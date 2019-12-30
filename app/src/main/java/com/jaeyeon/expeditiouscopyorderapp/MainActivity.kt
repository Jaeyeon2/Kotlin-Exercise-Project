package com.jaeyeon.expeditiouscopyorderapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jaeyeon.expeditiouscopyorderapp.ui.myPage.MyPageFragment
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    val user = FirebaseAuth.getInstance().currentUser
    internal var REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001
    private var gpsTracker: GpsTracker? = null
    internal var address: String? = null


    companion object {
        private val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private val GPS_ENABLE_REQUEST_CODE = 2001
        private val PERMISSIONS_REQUEST_CODE = 100
        lateinit var accUserEmail:String
        lateinit var accUserNickname:String
        lateinit var accUserPhotoUrl:String
        lateinit var accUserLatitude:String  // 위도
        lateinit var accUserLongitude:String // 경도
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view) as BottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_mySurrounding, R.id.navigation_shopList, R.id.navigation_myPrint
            )
        )
         */
        getUserLocation()
        auth = FirebaseAuth.getInstance()
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
        onStart()

        if(getIntent().getStringExtra("request_page") != null)
        {
            if(getIntent().getStringExtra("request_page").equals("SignupActivity"))
            {
                Log.d("request_page", "SignupActivity")
                navView.selectedItemId = R.id.navigation_myPage
            } else if(getIntent().getStringExtra("request_page").equals("UserInformation_logout"))
            {
                navView.selectedItemId = R.id.navigation_myPage
            } else if(getIntent().getStringExtra("request_page").equals("LoginActivity_loginSuccess"))
            {
                navView.selectedItemId = R.id.navigation_myPage
            }

        }

        user?.let {
            // Name, email address, and profile photo Url
            accUserNickname = user.displayName.toString()
            accUserPhotoUrl = user.photoUrl.toString()
            val emailVerified = user.isEmailVerified
            val uid = user.uid
        }
    }
    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?")
        builder.setCancelable(true)
        builder.setPositiveButton("설정", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
        })
        builder.setNegativeButton("취소", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.cancel()
            }
        })
        builder.create().show()
    }

    internal fun checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this@MainActivity, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE)
            }

        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.d("currentUser22", currentUser.toString())
        updateUI(currentUser)
    }


    private fun updateUI(user: FirebaseUser?) {
        //hideProgressDialog()
        if (user != null) {
            Log.d("접속한 사용자 이메일", user.email.toString())
            accUserEmail = user.email.toString()
        } else {
            Log.d("", "등록된 사용자 정보 없음")
            accUserEmail = "noLogin"
        }
    }

    fun getUserLocation() {

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show()
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "현재위 치 메모기능을 사용하기 위해 위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
                Toast.makeText(this, "현재 위치 메모기능을 사용하기 위해 위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()

            }
        }
        gpsTracker = GpsTracker(this@MainActivity)

        val latitude = gpsTracker!!.getLatitude()
        val longitude = gpsTracker!!.getLongitude()
        accUserLatitude = latitude.toString()
        accUserLongitude = longitude.toString()
        Log.d("latitudezzz", latitude.toString())
        address = getCurrentAddress(latitude, longitude)
        val str_address = address!!.split(" ")
        address = ""
        for (i in 1 until str_address.size) {
            address = address + str_address[i] + " "

            if (i == str_address.size - 1) {
                if (address!!.equals("미발견 ")) {
                    if (!checkLocationServicesStatus()) {
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("위치 서비스 비활성화")
                        builder.setMessage("해당 서비스를 이용하기 위해선 위치권한이 필요합니다..\n" + "위치 설정을 수정하실래요?")
                        builder.setCancelable(true)
                        builder.setPositiveButton("설정", object : DialogInterface.OnClickListener {
                            @Override
                            override fun onClick(dialog: DialogInterface, id: Int) {
                                val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
                            }
                        })
                        builder.setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            @Override
                            override fun onClick(dialog: DialogInterface, id: Int) {
                                dialog.cancel()
                            }
                        })
                        builder.create().show()
                    } else {
                        Log.d("address111", address)
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("주소 미발견")
                        builder.setMessage("현재 위치를 불러오는데 실패하였습니다. ")
                        builder.setPositiveButton("확인",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface, which: Int) {

                                }
                            })
                        builder.show()

                        checkRunTimePermission()
                    }
                } else {
                    Log.d("address11", address)
                }
            }
        }

    }
    fun getCurrentAddress(latitude: Double, longitude: Double): String {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7)
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"

        }



        if (addresses == null || addresses.size === 0) {
            return "주소 미발견"

        }

        val address = addresses!![0]
        return address.getAddressLine(0).toString() + "\n"
    }



}

package com.dr.predulive.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dr.predulive.*
import com.dr.predulive.daos.UserDao
import com.dr.predulive.dashboard.bottomNavigationViewFragments.coursesFragment
import com.dr.predulive.dashboard.bottomNavigationViewFragments.homeFragment
import com.dr.predulive.dashboard.bottomNavigationViewFragments.opportunityFragment
import com.dr.predulive.dashboard.bottomNavigationViewFragments.uploadFragment
import com.dr.predulive.dashboard.dashboardButtons.inspireSection.InspireActivity
import com.dr.predulive.dashboard.dashboardButtons.UploadShortVideoActivity
import com.dr.predulive.dashboard.dashboardButtons.employSection.EmployActivity
import com.dr.predulive.dashboard.uploadedCourses.UploadedCoursesActivity
import com.dr.predulive.models.User
import com.dr.predulive.navigationView.Student.StudentUploadedResumeActivity
import com.dr.predulive.navigationView.common.ChangePasswordActivity
import com.dr.predulive.navigationView.common.ContactUs
import com.dr.predulive.navigationView.common.EditProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var userType: String = ""
    private final val STUDENT = "Student"
    private final val INSTITUTE = "Institute"
    private final val COMPANY = "Company"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // full screen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_dashboard)

        auth = Firebase.auth

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar

        // main navigation view
        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.profile)


        // bottom navigation view
        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation_bar) as BottomNavigationView
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment()).commit()
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)

        //sets navigationView menu items by getting userType - student, company, Institute
        setNavigation()

    }

    //sets navigationView menu items by getting userType - student, company, Institute
    private fun setNavigation() {

        val userDao: UserDao = UserDao()

            userDao.getUserById(auth.uid.toString()).addOnSuccessListener {
                var user: User = it.toObject<User>()!!

                userType = user.userType

                when(userType) {
                    STUDENT -> {
                        navigationView.inflateMenu(R.menu.student_main_menu)
                    }
                    INSTITUTE -> {
                        navigationView.inflateMenu(R.menu.institute_main_menu)
                    }
                    COMPANY -> {
                        navigationView.inflateMenu(R.menu.company_main_menu)
                    }
                }

                if(userType == "") {
                    Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    finish()
                }

                updateNavigationDrawer(user.displayName, user.email, user.imageUrl)

            }

    }

    // update navigation drawer profile
    private fun updateNavigationDrawer(name: String, email: String, profileUrl: String) {

        var headerView: View = navigationView.getHeaderView(0)
        val nav_profile_name = headerView.findViewById<View>(R.id.nav_profile_name) as TextView
        val nav_profile_email = headerView.findViewById<View>(R.id.nav_profile_email) as TextView
        val nav_profile_image = headerView.findViewById<View>(R.id.nav_profile_image) as CircleImageView

        nav_profile_name.text = name
        nav_profile_email.text = email
        Glide.with(this)
            .load(profileUrl)
            .placeholder(R.drawable.loading_layout)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(nav_profile_image)
    }

    // bottom navigation view: switching fragments (home , opportunity, upload, courses)
    private var navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener() {
            var selectedFragment: Fragment = homeFragment()
            when (it.itemId) {
                R.id.home_item -> {
                    selectedFragment = homeFragment()
                }
                R.id.courses_item -> {
                    selectedFragment = coursesFragment()
                }
//                R.id.upload_item -> {
//                    selectedFragment = uploadFragment()
//                }
//                R.id.opportunity_item -> {
//                    selectedFragment = opportunityFragment()
//                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()

            return@OnNavigationItemSelectedListener true
        }

    // main navigation menu for userType
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

            // common
            R.id.profile -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.signOut -> {
                Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_SHORT).show()
                auth.signOut()
                finish()
            }
            R.id.changePasswords -> {
                val intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.contactUs -> {
                // open chrome custom tab
                ContactUs().loadContactUs(applicationContext, this@DashboardActivity)
            }
            R.id.Support -> {
                Toast.makeText(this, "R.id.Support", Toast.LENGTH_SHORT).show()
            }
            R.id.promotionalTools -> {
                Toast.makeText(this, "promotionalTools", Toast.LENGTH_SHORT).show()
            }

            //  userSpecific :- Company
            R.id.CompanyDashboarduploadCompanyDetails -> {
                Toast.makeText(this, "R.id.CompanyDashboarduploadCompanyDetails", Toast.LENGTH_SHORT).show()
            }
            R.id.CompanyDashboardUploadCandidateRequirements -> {
                Toast.makeText(this, "R.id.CompanyDashboardUploadCandidateRequirements", Toast.LENGTH_SHORT).show()
            }


            //  userSpecific :- Institute
            R.id.InstituteDashboardUploadedInstituteDetails -> {
                Toast.makeText(this, "R.id.InstituteDashboardUploadedInstituteDetails", Toast.LENGTH_SHORT).show()
            }
            R.id.InstituteDashboardUploadCourses -> {
                Toast.makeText(this, "R.id.InstituteDashboardUploadCourses", Toast.LENGTH_SHORT).show()
            }



            //  userSpecific :- Student
            R.id.studentuploadedResume -> {
                Toast.makeText(this, "R.id.studentuploadedResume", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, StudentUploadedResumeActivity::class.java)
                startActivity(intent)
            }
            R.id.studentUploaded_Short_Videos -> {
//                Toast.makeText(this, "R.id.studentUploaded_Short_Videos", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UploadShortVideoActivity::class.java)
                startActivity(intent)
            }
            R.id.studentuploadedCourses -> {
                Toast.makeText(this, "R.id.studentuploadedCourses", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UploadedCoursesActivity::class.java)
                startActivity(intent)
            }
            R.id.studentStudyMaterial -> {
                Toast.makeText(this, "R.id.studentStudyMaterial", Toast.LENGTH_SHORT).show()
            }

//            R.id.nav_login -> {
//                Toast.makeText(this, "R.id.nav_login", Toast.LENGTH_SHORT).show()
////                menu.findItem(R.id.nav_logout).setVisible(true)
////                menu.findItem(R.id.nav_profile).setVisible(true)
////                menu.findItem(R.id.nav_login).setVisible(false)
//            }
//            R.id.nav_logout -> {
//                Toast.makeText(this, "R.id.nav_logout", Toast.LENGTH_SHORT).show()
////                menu.findItem(R.id.nav_logout).setVisible(false)
////                menu.findItem(R.id.nav_profile).setVisible(false)
////                menu.findItem(R.id.nav_login).setVisible(true)
//            }
//            R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun inspireButtonClicked(view: View) {
        val intent = Intent(this, InspireActivity::class.java)
        startActivity(intent)
    }
    fun awareButtonClicked(view: View) {}
    fun educateButtonClicked(view: View) {
        var intent = Intent(this, UploadedCoursesActivity::class.java)
        startActivity(intent)
    }
    fun employButtonClicked(view: View) {
        val intent = Intent(this, EmployActivity::class.java)
        startActivity(intent)
    }

}
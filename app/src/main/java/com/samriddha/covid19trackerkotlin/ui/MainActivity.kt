package com.samriddha.covid19trackerkotlin.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.samriddha.covid19trackerkotlin.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme_NoActionBar) // Changing Activity Theme from SplashTheme to AppTheme.NoActionBar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //back button for every fragment
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val menu = navigationView.menu;
        val itemHeading1 = menu.findItem(R.id.navTitle1);
        val itemHeading2 = menu.findItem(R.id.navTitle2);
        val spannableString1 = SpannableString(itemHeading1.title);
        val spannableString2 = SpannableString(itemHeading2.title);
        spannableString1.setSpan(
            TextAppearanceSpan(this, R.style.NavigationDrawerText),
            0,
            spannableString1.length,
            0
        )
        spannableString2.setSpan(
            TextAppearanceSpan(this, R.style.NavigationDrawerText),
            0,
            spannableString2.length,
            0
        )
        itemHeading1.title = spannableString1
        itemHeading2.title = spannableString2


        //Setting Up Navigation Controller With drawer layout and navigation view
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {

            if (navController.currentDestination?.id == R.id.globalDataFragment) {

                val toast = Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT)

                // Back press twice to exit the app
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    toast.cancel();
                    super.onBackPressed();
                    return;

                } else {
                    toast.show()
                }

                backPressedTime = System.currentTimeMillis();

            } else {
                super.onBackPressed();
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerLayout.closeDrawers()
        item.isChecked = true

        return when (item.itemId) {
            R.id.globalDataFragment -> if (navController.currentDestination
                    ?.id == R.id.globalDataFragment
            ) false else {
                navController.navigate(R.id.globalDataFragment)
                true
            }
            R.id.countriesFragment -> if (navController.currentDestination
                    ?.id == R.id.countriesFragment
            ) false else {
                navController.navigate(R.id.countriesFragment)
                true
            }
            R.id.indiaStatFragment -> if (navController.currentDestination
                    ?.id == R.id.indiaStatFragment
            ) false else {
                navController.navigate(R.id.indiaStatFragment)
                true
            }
            R.id.aboutAppFragment -> if (navController.currentDestination
                    ?.id == R.id.aboutAppFragment
            ) false else {
                navController.navigate(R.id.aboutAppFragment)
                true
            }
            R.id.shareApp -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Covid Tracker App")
                    var shareMessage =
                        "\nHey,I am sharing this awesome app with you !! Download it from below link.\n\n"
                    shareMessage += "https://drive.google.com/drive/folders/1XhIiMM9lhPRJagxfue9Pwd--cGdRS3vV?usp=sharing"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "Share Using"))
                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                }
                true
            }
            else -> false
        }


    }


}
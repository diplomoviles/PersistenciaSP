package com.amaurypm.persistenciasp

import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.amaurypm.persistenciasp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: Fragment

    /*private lateinit var sp: SharedPreferences
    private lateinit var spEditor: SharedPreferences.Editor*/

    private lateinit var encryptedSp: SharedPreferences
    private lateinit var encryptedSpEditor: SharedPreferences.Editor

    private lateinit var user_sp: String
    private lateinit var password_sp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        /*sp = getPreferences(Context.MODE_PRIVATE)
        spEditor = sp.edit()*/

        val masterKey = MasterKey.Builder(
            this,
            MasterKey.DEFAULT_MASTER_KEY_ALIAS
        ).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSp = EncryptedSharedPreferences.create(
            this,
            "encrypted_sp",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        encryptedSpEditor = encryptedSp.edit()



        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as Fragment

        val color = encryptedSp.getInt("color", R.color.white) //ponemos como valor por defecto el blanco

        //val name = sp.getString("name", "")

        changeColor(color)

        //Toast.makeText(this, "Hola $name", Toast.LENGTH_LONG).show()


        //navHostFragment.view?.setBackgroundColor(Color.BLUE)
        //navHostFragment.view?.setBackgroundColor(Color.rgb(100,30,40))

        //Utilizando el archivo de shared preferences encriptadas:

        //user_sp = encryptedSp.getString("name", "").toString()

        //Toast.makeText(this, "El usuario es: $user_sp", Toast.LENGTH_LONG).show()


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_azul -> {
                changeColor(R.color.mi_azul)
                true
            }

            R.id.action_rojo -> {
                changeColor(R.color.mi_rojo)
                true
            }

            R.id.action_verde -> {
                changeColor(R.color.mi_verde)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun changeColor(color: Int) {
        navHostFragment.view?.setBackgroundColor(getColor(color))
        saveColor(color)
    }

    private fun saveColor(color: Int) {
        /*spEditor.putInt("color", color)
        spEditor.putString("name", "Amaury")
        spEditor.putBoolean("value", true).apply()*/
        encryptedSpEditor.putInt("color", color)
        encryptedSpEditor.putString("name", "Amaury")
        encryptedSpEditor.putBoolean("name", false)
        encryptedSpEditor.putString("password", "password").apply()
    }
}

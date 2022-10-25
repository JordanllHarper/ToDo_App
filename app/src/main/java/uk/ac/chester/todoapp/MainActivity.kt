package uk.ac.chester.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import uk.ac.chester.todoapp.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {

    private var listItems = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->


            if (result.resultCode == Activity.RESULT_OK) {
                val task = result.data?.getStringExtra("task_text")!!


                listItems.add(task)
                //Saving tasks to file
                Log.d("here", "Got here")
                saveTasks()
                adapter.notifyDataSetChanged()

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTasks()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
        adapter = ArrayAdapter<String>(this, R.layout.list_layout, listItems)
        binding.listView.adapter = adapter

        registerForContextMenu(binding.listView)


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.longpress, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteTask(item)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun createTask() {
        val intent = Intent(this, EditTaskActivity::class.java)
        resultLauncher.launch(intent)


    }

    private fun deleteTask(item: MenuItem) {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        listItems.removeAt(info.position)
        adapter.notifyDataSetChanged()

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
            R.id.menu_insert -> {
                createTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadTasks() {
        Thread{
            try {
                val file = File(filesDir, "tasks.dat")  //Loading from files directory
                val fis = FileInputStream(file)
                val ois = ObjectInputStream(fis)

                listItems = ois.readObject() as MutableList<String> //ignore

            } catch (e: IOException) {
                println(e.stackTrace)
                listItems = mutableListOf()
            }

        }.start()

    }

    private fun saveTasks() {

        Thread{
            try {
                Log.d("saving", "Saving")
                val file = File(filesDir, "tasks.dat") //Saving to files directory
                val fos = FileOutputStream(file)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(listItems)
                oos.close()
                fos.close()
                Log.d("save", "Saved")


            } catch (e: IOException) {
                Log.e("Stack trace: ", e.message.toString())
            }
        }.start()


    }


}
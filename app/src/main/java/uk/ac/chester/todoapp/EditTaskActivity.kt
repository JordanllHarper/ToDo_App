package uk.ac.chester.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import uk.ac.chester.todoapp.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityEditTaskBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.addButton.setOnClickListener {

            val intent = Intent()
            intent.putExtra("task_text", binding.addTaskText.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }


        binding.cancelButton.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }




}
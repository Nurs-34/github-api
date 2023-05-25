package kg.surfit.testweekapp3.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kg.surfit.testweekapp3.databinding.ActivityMainBinding
import kg.surfit.testweekapp3.ui.adapter.GitRepoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GitRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        adapter = GitRepoAdapter()

        binding.recyclerViewList.adapter = adapter

        lifecycleScope.launch {
            binding.editTextUsername.doOnTextChanged { text, _, _, _ ->
                viewModel.setText(text.toString())
            }
        }

        //не успел додумать как сделать здесь красивее (это типа init)
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                delay(1500)
                adapter.updateData(viewModel.repoList.value)
                Picasso.get().load(viewModel.user.value?.avatar.toString())
                    .into(binding.imageViewUserIcon)
            }
        }

        viewModel.mainActionFlow.onEach { action ->
            when (action) {
                is MainAction.ShowErrorToast -> {
                    Toast.makeText(
                        applicationContext, "Вы ввели неверный username", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    fun getAll(view: View) {
        viewModel.getAll(adapter)
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                delay(500)
                Picasso.get().load(viewModel.user.value?.avatar.toString())
                    .into(binding.imageViewUserIcon)
            }
        }
    }
}
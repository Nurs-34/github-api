package kg.surfit.testweekapp3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.surfit.testweekapp3.databinding.ItemGitRepoBinding
import kg.surfit.testweekapp3.models.GitRepo

class GitRepoAdapter() :
    RecyclerView.Adapter<GitRepoAdapter.GitRepoViewHolder>() {

    private var gitRepoList: List<GitRepo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGitRepoBinding.inflate(inflater, parent, false)
        return GitRepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        val gitRepo = gitRepoList[position]
        holder.bind(gitRepo)
    }

    override fun getItemCount(): Int {
        return gitRepoList.size
    }

    inner class GitRepoViewHolder(private val binding: ItemGitRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gitRepo: GitRepo) {
            binding.textViewRepoName.text = gitRepo.name
            binding.textViewRepoDesc.text = gitRepo.desc
        }
    }

    fun updateData(newList: List<GitRepo>) {
        gitRepoList = newList
        notifyDataSetChanged()
    }
}

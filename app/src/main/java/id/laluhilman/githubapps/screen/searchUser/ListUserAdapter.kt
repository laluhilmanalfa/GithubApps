package id.laluhilman.githubapps.screen.searchUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.laluhilman.githubapps.databinding.ItemUserLayoutBinding
import id.laluhilman.githubapps.entity.GithubUser

class ListUserAdapter(val listUser : ArrayList<GithubUser>) :
    RecyclerView.Adapter<ListUserAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
       return MovieViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listUser.get(position))
    }


    inner class MovieViewHolder(val binding : ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind( data: GithubUser) {
            binding.data = data
            Glide
                .with(binding.itemUserImage.context)
                .load(data.profileUrl)
                .fitCenter()
                .into(binding.itemUserImage);
            binding.executePendingBindings()

        }
    }

}
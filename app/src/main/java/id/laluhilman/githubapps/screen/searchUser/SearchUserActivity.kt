package id.laluhilman.githubapps.screen.searchUser

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.widget.RxTextView
import id.laluhilman.githubapps.R
import id.laluhilman.githubapps.databinding.SearchLayoutActivityBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SearchUserActivity : AppCompatActivity() {

    private val viewModel : SearchUserViewModel by inject()
    lateinit var binding : SearchLayoutActivityBinding
    private lateinit var adapter : ListUserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<SearchLayoutActivityBinding>(this, R.layout.search_layout_activity)

        with(binding){
            lifecycleOwner = this@SearchUserActivity
            viewModel = this.viewModel
        }

        with(viewModel){
            refreshList.observe(this@SearchUserActivity, Observer { refreshList() })
            isInitialLoading.observe(this@SearchUserActivity, Observer { showLoading( binding.searchUserCenterLoading, it) })
            isLoadingLoadmore.observe(this@SearchUserActivity, Observer { showLoading(binding.searchUserBottomLoading, it) })
            isUserFound.observe(this@SearchUserActivity, Observer { showUserNotFoundLabel(!it) })
            errorRequest.observe(this@SearchUserActivity, Observer { showError( it) })
        }
        setUpRecyclerView()
        setUpRxBinding()
    }


    private fun setUpRecyclerView() {
        adapter = ListUserAdapter(viewModel.listGithubUserModel)
        var layoutManager = LinearLayoutManager(this)
        binding.searchUserListUser.layoutManager = layoutManager
        binding.searchUserListUser.itemAnimator = DefaultItemAnimator()
        binding.searchUserListUser.adapter = adapter
        binding.searchUserListUser.addOnScrollListener( object  : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var totalItemCount = layoutManager.itemCount-1
                var lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if(totalItemCount == lastVisibleItem && !binding.searchUserBottomLoading.isVisible && dy >0){
                    viewModel.handleLoadMore()
                }
            }
        })
    }

    private fun showLoading(view : View, isLoading : Boolean){
        if(isLoading) {
            binding.searchUserCenterLoading.startShimmerAnimation()
            view.visibility = View.VISIBLE
        }
        else {
            view.visibility = View.GONE
            binding.searchUserCenterLoading.stopShimmerAnimation()
        }
    }

    private fun refreshList(){
        adapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage : String){
        Snackbar.make(binding.searchUserListUser, errorMessage, Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }

    private fun showUserNotFoundLabel(isShow : Boolean){
        if(isShow)
            binding.searchUserNoUserFound.visibility = View.VISIBLE
        else
            binding.searchUserNoUserFound.visibility = View.GONE

    }

    @SuppressLint("CheckResult")
    private fun setUpRxBinding(){
        RxTextView.textChanges(binding.searchUserSearchEdittext)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.searchUser(it.toString())
            };
    }

}
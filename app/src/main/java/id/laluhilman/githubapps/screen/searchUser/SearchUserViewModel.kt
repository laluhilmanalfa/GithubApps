package id.laluhilman.githubapps.screen.searchUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.laluhilman.githubapps.entity.GithubUser
import id.laluhilman.githubapps.repo.GithubUserRepo
import id.laluhilman.githubapps.util.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchUserViewModel(val githubUserRepo: GithubUserRepo) : ViewModel() {

    private var compositeDisposable  = CompositeDisposable()
    val isLoadingLoadmore : MutableLiveData<Boolean> = MutableLiveData(false)
    val isInitialLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    val isUserFound : MutableLiveData<Boolean> = MutableLiveData()
    val refreshList : SingleLiveEvent<Boolean> = SingleLiveEvent()
    val errorRequest : SingleLiveEvent<String> = SingleLiveEvent<String>()
    val listGithubUserModel : ArrayList<GithubUser> = ArrayList()
    private var pageCount = 1
    private var username = "";

    fun loadUser (){
        if(username.length==0)
            return
        showLoading(true)
        compositeDisposable.add(

            githubUserRepo.getUser(username, pageCount,10)
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    showLoading(false)
                    addUser(t.items)
                }, {
                    showLoading(false)
                    handleError(it)
                })

        )
    }

    fun addUser(listUser : ArrayList<GithubUser>){
        refreshList.value = true
        listGithubUserModel.addAll(listUser)
        pageCount++
        isUserFound.value = listGithubUserModel.size != 0

    }

    fun handleLoadMore(){
        if(!isLoadingLoadmore.value!!){
            loadUser()
        }
    }


    fun searchUser(usernameParam :String){
        username  = usernameParam
        pageCount = 1;
        listGithubUserModel.clear()
        refreshList.value = true
        isUserFound.value = true
        loadUser()
    }

    private fun showLoading( isShowLoading : Boolean){
        if(pageCount == 1 )
            isInitialLoading.value = isShowLoading
        else
            isLoadingLoadmore.value = isShowLoading
    }

    private fun handleError(error : Throwable){
         if(error.message.equals("HTTP 403 rate limit exceeded"))
            errorRequest.postValue("To much request, please wait a moment ")
        else if(error.message!!.contains("Unable to resolve host"))
            errorRequest.postValue("Can't Connect to Server, Please make sure you have internet connection")
        else
            errorRequest.postValue(error.toString())
    }

}
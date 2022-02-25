package com.example.used_trading.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.used_trading.DBKey.Companion.CHILD_CHAT
import com.example.used_trading.DBKey.Companion.DB_ARTICLES
import com.example.used_trading.DBKey.Companion.DB_USERS
import com.example.used_trading.R
import com.example.used_trading.chatlist.ChatListItem
import com.example.used_trading.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference

    private var binding: FragmentHomeBinding? = null
    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)      //  getValue로 Model class 자체를 DB에 데이터를 올리고 내려받음 (data class에 생성자 필요)
            articleModel ?: return      // ArticleModel이 null이라면 return

            articleList.add(articleModel)       //  null이 아니라면
            articleAdapter.submitList(articleList)      //  articleAdapter에 articleList를 넣어줌
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)        // null이 될 수 없도록 지역변수로 binding 선언
        binding = fragmentHomeBinding

        articleList.clear()     // List 초기화
        userDB = Firebase.database.reference.child(DB_USERS)        //  user DB 초기화
        articleDB = Firebase.database.reference.child(DB_ARTICLES)      //  article DB 초기화
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->

            if (auth.currentUser != null) {     //  로그인을 한 상태

                if (auth.currentUser.uid != articleModel.sellerId)  {       //  현재 사용자 id와 아이템을 등록한 사용자의 id가 같지 않다면

                    val chatRoom = ChatListItem (
                        buyerId = auth.currentUser.uid,
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis()
                    )

                    userDB.child(auth.currentUser.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()

                } else {         //  현재 사용자 id와 아이템을 등록한 사용자의 id가 같다면
                    Snackbar.make(view, "내가 등록한 아이템입니다", Snackbar.LENGTH_LONG).show()
                }

            } else {        //  로그인을 안 한 상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }
        })

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            if (auth.currentUser != null) {     //  로그인을 한 User인 경우에만 글 작성 가능
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }
        }

        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()

        articleAdapter.notifyDataSetChanged()       //  view를 다시 그림
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}
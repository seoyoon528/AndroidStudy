package com.example.used_trading.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.used_trading.DBKey.Companion.CHILD_CHAT
import com.example.used_trading.DBKey.Companion.DB_USERS
import com.example.used_trading.R
import com.example.used_trading.chatdetail.ChatRoomActivity
import com.example.used_trading.databinding.FragmentChatlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private var binding: FragmentChatlistBinding? = null
    private lateinit var chatDB: DatabaseReference

    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        chatListAdapter = ChatListAdapter( onItemClicked = { chatRoom ->
            // 채팅방으로 이동
            context?.let {          // Fragment에서는 Intent()에 this 대신 context를 넣어줘야함. But, context는 nullable이기 때문에 위에서 ?.let으로  null check를 걸어주고 it으로 접근하면 됨
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)

                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if (auth.currentUser == null) {     //  로그인이 안되어 있을 시 return
            return
        }

        chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser.uid).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {     // SingleValue :: DB_USERS.auth.currentUser.uid.CHILD_CHAT 전체
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {     //  전체 chat 안의 각각의 value(각 채팅)를 가져오기 위해서 children을 forEach문으로 잘라서 snapshot
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
//                    Log.d("Chat List 추가 여부 확인", "List에 추가된 Chat ${model.itemTitle}")
                }
                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()      //  view 갱신
    }
}
package com.main.carsales.views

import android.view.View
import com.main.carsales.R
import com.main.carsales.databinding.ChatFromUserRowBinding
import com.main.carsales.databinding.ChatToUserRowBinding
import com.xwray.groupie.viewbinding.BindableItem


class ChatFromItem(val text: String): BindableItem<ChatFromUserRowBinding>() {
    override fun bind(viewBinding: ChatFromUserRowBinding, position: Int) {
        viewBinding.textViewMessage.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_user_row
    }

    override fun initializeViewBinding(view: View): ChatFromUserRowBinding =
        ChatFromUserRowBinding.bind(view)

}

class ChatToItem(val text: String, val user: com.main.carsales.data.User): BindableItem<ChatToUserRowBinding>() {
    override fun bind(viewBinding: ChatToUserRowBinding, position: Int) {
        viewBinding.textViewMessage.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_user_row
    }

    override fun initializeViewBinding(view: View): ChatToUserRowBinding =
        ChatToUserRowBinding.bind(view)

}
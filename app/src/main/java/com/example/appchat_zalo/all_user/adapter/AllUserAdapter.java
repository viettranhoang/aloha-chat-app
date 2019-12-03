package com.example.appchat_zalo.all_user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat_zalo.R;
import com.example.appchat_zalo.all_user.listener.OnclickItemUserListener;
import com.example.appchat_zalo.model.Users;
import com.example.appchat_zalo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.AllUserViewHolder> {
    private List<Users> mListUser = new ArrayList<>();

    private OnclickItemUserListener listener;

    public AllUserAdapter(OnclickItemUserListener listener) {
        this.listener = listener;
    }

    public void setmListUser(List<Users> mListUser) {
        this.mListUser = mListUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_item, parent, false);
        return new AllUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserViewHolder holder, int position) {
        holder.bindata(mListUser.get(position));

    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }

    public class AllUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_avatar)
        ImageView mImageAvatar;

        @BindView(R.id.image_online)
        ImageView mImageOnline;

        @BindView(R.id.text_name)
        TextView mTextName;

        public AllUserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        void bindata(Users users){

            if (users != null) {
                mTextName.setText(users.getName());

                try {
                    if (users.getAvatar().equals("default")) {
                        mImageAvatar.setImageResource(R.drawable.background_main);
                    } else {
                        Glide.with(itemView)
                                .load(users.getAvatar())
                                .circleCrop()
                                .into(mImageAvatar);
                    }
                }
                catch (NullPointerException exNull){

                    Toast.makeText(itemView.getContext(), "message" + exNull, Toast.LENGTH_SHORT).show();
                }



                if (users.getOnline() == Constants.ONLINE) {
                    mImageOnline.setVisibility(View.VISIBLE);
                    Glide.with(itemView)
                            .load(users.getOnline())
                            .into(mImageOnline);
                } else {
                    mImageOnline.setVisibility(View.GONE);
                }
//            Glide.with(itemView)
//                    .load(users.getOnline())
//                    .into(mImageOnline);
            }
        }

        @OnClick(R.id.layout_user)
        void  onClickItem(){
            listener.onClickFriendOnlineItem(mListUser.get(getAdapterPosition()));

        }
    }
}

package com.example.xiaoxiao.materialdesign;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xiaoxiao.materialdesign.databinding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<User> users =null;
    private RecyclerView recyclerView;
    BindAdapter bindAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);
        recyclerView =findViewById(R.id.recycle_view);
        randomUser();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        bindAdapter =new BindAdapter();
        bindAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClic(View view, int position) {
                Toast.makeText(MainActivity.this,"position: "+position,Toast.LENGTH_SHORT).show();
                removeData(position);
            }
        });
        recyclerView.setAdapter(bindAdapter);
        recyclerView.addItemDecoration(new MyDecoration(this,LinearLayoutManager.VERTICAL,R.drawable.decoration_line));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        com.example.xiaoxiao.materialdesign.databinding.ActivityMainBinding binding =DataBindingUtil.setContentView(this,R.layout.activity_main);
//        User user = new User("ma","ga");
//        binding.setUser(user);
    }

    public void removeData(int position){
        users.remove(position);
        bindAdapter.notifyDataSetChanged();
    }
    public class BindAdapter extends RecyclerView.Adapter<BindAdapter.BindViewHolder> implements View.OnClickListener{

        OnItemClickListener onItemClickListener =null;
        public BindAdapter(){

        }


        public class BindViewHolder extends RecyclerView.ViewHolder {
             ItemUserBinding viewDataBinding = null;

            public BindViewHolder(View view) {
                super(view);
                viewDataBinding =DataBindingUtil.bind(view);

            }

            public void bind(User user){
                viewDataBinding.setUser(user);
            }

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener !=null){
                onItemClickListener.OnItemClic(v,(int)v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.onItemClickListener =onItemClickListener;
        }

        @Override
        public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(MainActivity.this).inflate(R.layout.item_user,parent,false);
            view.setOnClickListener(this);
            BindViewHolder bindViewHolder =new BindViewHolder(view);
            return bindViewHolder;
        }

        @Override
        public void onBindViewHolder(BindViewHolder holder, int position) {
                    holder.bind(users.get(position));
                    holder.itemView.setTag(position);

        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    public void randomUser(){
        users =new ArrayList<>();
        for(int i=0;i<10;i++){
            User user =new User(Randoms.getFirstName(),Randoms.getLastName());
            users.add(user);
        }
    }
    public interface OnItemClickListener{
        void OnItemClic(View view,int position);
    }

}

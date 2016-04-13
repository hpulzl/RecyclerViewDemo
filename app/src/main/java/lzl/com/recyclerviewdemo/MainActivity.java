package lzl.com.recyclerviewdemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lzl.com.recyclerviewdemo.adapter.MainAdapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<>();
    private MainAdapter adapter = new MainAdapter(this,list);
    private LinearLayoutManager mLayoutManager;
    private int lastItem;
    private final int totalPage = 36;  //设置总共的数据
    private int pageNum=1;
    private View footView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //设置刷新条
        initSwipeRefresh();
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        //初始化，一直显示第一条数据。
        getList(1);
        //在设置adapter之前要先获取list集合。
        recyclerView.setAdapter(adapter);
        footView = LayoutInflater.from(this).inflate(R.layout.recycler_foot,recyclerView,false);
        adapter.setFootView(footView);
        setRecyclerScrollListener();
    }

    private void setRecyclerScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               if(lastItem+1 == adapter.getItemCount() && newState == RecyclerView.SCROLL_STATE_IDLE){
                   //最后一个item并且还有下滑的趋势
                   if(lastItem+1 <= totalPage){
                       getList(pageNum);
                   }else{
                       TextView loadMoreTv = (TextView) footView.findViewById(R.id.loadMore_tv);
                       ProgressBar loadMorePb = (ProgressBar) footView.findViewById(R.id.loadMore_pb);
                       loadMorePb.setVisibility(View.GONE);
                       loadMoreTv.setText("没有更多数据");
                   }
               }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 初始化下拉刷新条。
     */
    private void initSwipeRefresh(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //设置刷新条的颜色
        swipeRefreshLayout.setColorSchemeColors(
                R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);

        //第一次进入页面时，显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false,0,(int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeReFreshState(true);  //开始进入界面，显示刷新条
    }

    private void swipeReFreshState(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onRefresh() {
        //先清空list集合中的数据
        list.clear();
        //刷新第一页数据
        getList(1);
    }

    /**
     * 这里模拟分页，出入的是页数
     * 然后根据每页显示的个数进行更新数据。
     * @param pageNo
     */
    public void getList(int pageNo){
        pageNum = pageNo;
        int pageCount = 6;   //每页显示6条数据。
        for(int i=(pageNo-1)*pageCount;i<pageCount * pageNo;i++){
            if(i == totalPage){
                return;
            }
            list.add("显示第"+(i+1)+"条数据");
        }
        pageNum++;
        adapter.notifyDataSetChanged();
        //数据更新以后，刷新条消失
        swipeReFreshState(false);
    }
}

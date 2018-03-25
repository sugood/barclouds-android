package com.barclouds.views.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.bean.Info;
import com.barclouds.views.bean.PageBean;
import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.dialog.CustomProgressDialog;
import com.barclouds.views.domain.InfoIndex;
import com.barclouds.views.domain.User;
import com.barclouds.views.interfaces.Netcallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 带滑动表头与固定列的ListView
 */
public class InfoListViewActivity extends BaseActivity implements
         OnClickListener{
    private ImageButton imagebutton_setting;
    private ImageButton imagebutton_search;
    private ImageButton imagebutton_refresh;
    private ImageButton imagebutton_delete;

    private TextView textViewField0;
    private TextView textViewField1;
    private TextView textViewField2;
    private TextView textViewField3;
    private TextView textViewField4;
    private TextView textViewField5;
    private TextView textViewField6;
    private TextView textViewField7;
    private TextView textViewField8;
    private TextView textViewField9;

    private User user;

    private ListView mListView;
    //方便测试，直接写的public
    public HorizontalScrollView mTouchView;
    //装入所有的HScrollView
    protected List<InfoScrollView2> mHScrollViews =new ArrayList<InfoScrollView2>();
    private String[] cols = new String[] { "title", "data_1", "data_2", "data_3", "data_4", "data_5",
         "data_6","data_7","data_8", "data_9",};
    private  ScrollAdapter mAdapter;

    private boolean is_divPage;// 是否进行分页操作
    private static int pageNo = 1;//设置pageNo的初始化值为1，即默认获取的是第一页的数据。
    private static int totalPage; //总页数
    private static int totalCount;//总数量
    private static int pageSize;//每页的数量
    private int curTotalCount=0;//当前数量
    private int curTotalPage=0;//当前总页数

    private List<Map<String, String>> datas;  //用来存放获取的所有数据

    public List<Info> datasend;
//    List<Map<String, String>> datas = new ArrayList<Map<String,String>>();

    private CustomProgressDialog dialog;
    //底部View
    private View footerView;

    private String selectFlag="page";//将标志设置为page，表示正在浏览数据；设置为search，表示正在搜索操作
    private String field0;

    private AppContext appContext;
    long exitTime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_hlistview);
        Log.d("BarcloudsDebug", "InfoListViewActivity.onCreate");
		//init();
	}

	void init() {
        Log.d("BarcloudsDebug","InfoListViewActivity.init");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_infolistview);
//		List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
//		Map<String, String> data = null;

        //获取当前用户数据列名
        appContext = (AppContext) getApplicationContext();
        InfoIndex infoIndex = appContext.getInfoIndex();
        user =appContext.getUser();

        textViewField0=(TextView)findViewById(R.id.textViewFiled0);
        textViewField1=(TextView)findViewById(R.id.textViewFiled1);
        textViewField2=(TextView)findViewById(R.id.textViewFiled2);
        textViewField3=(TextView)findViewById(R.id.textViewFiled3);
        textViewField4=(TextView)findViewById(R.id.textViewFiled4);
        textViewField5=(TextView)findViewById(R.id.textViewFiled5);
        textViewField6=(TextView)findViewById(R.id.textViewFiled6);
        textViewField7=(TextView)findViewById(R.id.textViewFiled7);
        textViewField8=(TextView)findViewById(R.id.textViewFiled8);
        textViewField9=(TextView)findViewById(R.id.textViewFiled9);

        //防止为空
        if(infoIndex!=null) {
            textViewField0.setText(infoIndex.getFieldName0());
            textViewField1.setText(infoIndex.getFieldName1());
            textViewField2.setText(infoIndex.getFieldName2());
            textViewField3.setText(infoIndex.getFieldName3());
            textViewField4.setText(infoIndex.getFieldName4());
            textViewField5.setText(infoIndex.getFieldName5());
            textViewField6.setText(infoIndex.getFieldName6());
            textViewField7.setText(infoIndex.getFieldName7());
            textViewField8.setText(infoIndex.getFieldName8());
            textViewField9.setText(infoIndex.getFieldName9());
        }else{
            textViewField0.setText("第一列");
            textViewField1.setText("第二列");
            textViewField2.setText("第三列");
            textViewField3.setText("第四列");
            textViewField4.setText("第五列");
            textViewField5.setText("第六列");
            textViewField6.setText("第七列");
            textViewField7.setText("第八列");
            textViewField8.setText("第九列");
            textViewField9.setText("第十列");
        }
        imagebutton_setting = (ImageButton) findViewById(R.id.imagebutton_setting);
        imagebutton_search = (ImageButton) findViewById(R.id.imagebutton_search);
        imagebutton_refresh = (ImageButton) findViewById(R.id.imagebutton_refresh);
        imagebutton_delete = (ImageButton)findViewById(R.id.imagebutton_delete);

        imagebutton_setting.setOnClickListener(this);
        imagebutton_search.setOnClickListener(this);
        imagebutton_refresh.setOnClickListener(this);
        imagebutton_delete.setOnClickListener(this);

        LayoutInflater mInflater = getLayoutInflater();
        footerView = mInflater.inflate(R.layout.footer, null);
        footerView.setVisibility(View.GONE);

        InfoScrollView2 headerScroll = (InfoScrollView2) findViewById(R.id.item_scroll_title);
		//添加头滑动事件
		mHScrollViews.add(headerScroll);
		mListView = (ListView) findViewById(R.id.hlistview_scroll_list);

        Log.d("BarcloudsDebug","InfoListViewActivity.initListview");
        initListview();//初始化ListView数据

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /**
                 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=总页数时，加载更多数据。
                 */
//                Log.d("BarCloudsDebug:", "totalPage=" + totalPage);
//                Log.d("BarCloudsDebug:", "pageNo=" + pageNo);
//                Log.d("BarCloudsDebug:", "is_divPage=" + is_divPage);
                if (is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (pageNo <= totalPage){
                        loading();
                        footerView.setVisibility(View.VISIBLE);
                    } else if (pageNo > totalPage ) {
                        footerView.setVisibility(View.GONE);
                        Toast.makeText(InfoListViewActivity.this, "没有更多数据...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            /**
             * 当：第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=所有的item总数的时候，
             * is_divPage变为TRUE，这个时候才会加载数据。
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
//                Log.d("BarCloudsDebug:","totalItemCount="+totalItemCount);
//                Log.d("BarCloudsDebug:","firstVisibleItem="+firstVisibleItem);
//                Log.d("BarCloudsDebug:","visibleItemCount="+visibleItemCount);
            }
        });

        //添加底部View
        mListView.addFooterView(footerView);
	}

    public void loading() {
        NetRequestConstant nrc = new NetRequestConstant();
        // post请求
        nrc.setType(HttpRequestType.POST);
        NetRequestConstant.requestUrl = NetUrlConstant.INFOPAGE;
        NetRequestConstant.context = this;
        Map<String, Object> map = new HashMap<String, Object>();

        if (selectFlag.equals("page")){
            map.put("selectFlag", "page");
            map.put("uid", user.getUsername());
            map.put("pageNo", "" + pageNo);
        }else if(selectFlag.equals("search")){
            map.put("selectFlag", "search");
            map.put("uid", user.getUsername());
            map.put("pageNo", "" + pageNo);
            map.put("field0",field0);
        }
        NetRequestConstant.map = map;

        getServer(new Netcallback() {

            public void preccess(Object res, boolean flag) {
                if (res != null) {
                    try {
                        Map<String, String> data = null;
                        Gson gson = new Gson();
//                        Log.d("BarcloudsDebug","showhtml"+(String) res);
                        PageBean<Info> page = gson.fromJson((String) res,
                                new TypeToken<PageBean<Info>>() {
                                }.getType());

                        datasend = page.getBeanList();
                        totalPage = Integer.valueOf(page.getTotalPage());
                        totalCount=  Integer.valueOf(page.getTotalCount());
                        pageSize= Integer.valueOf(page.getPageSize());
//                        Log.d("BarcloudsDebug","showbean:"+datasend.get(8).getField1());

                        for (int i = 0; i < datasend.size(); i++) {
                            //获取一行DataBean
                            Info dataBean = datasend.get(i);

                            data = new HashMap<String, String>();
//                            data.put("title", "C区_" + i);
                            data.put("title", dataBean.getField0());
                            data.put("data_1", dataBean.getField1());
                            data.put("data_2", dataBean.getField2());
                            data.put("data_3", dataBean.getField3());
                            data.put("data_4", dataBean.getField4());
                            data.put("data_5", dataBean.getField5());
                            data.put("data_6", dataBean.getField6());
                            data.put("data_7", dataBean.getField7());
                            data.put("data_8", dataBean.getField8());
                            data.put("data_9", dataBean.getField9());
                            datas.add(data);
                        }

//                        Log.d("BarcloudsDebug:","datassize="+datas.size());
                        mAdapter.bindData(datas);
                        /**
                         * 当pageNo等于1的时候才会setAdapter，以后不会再设置，直接notifyDataSetChanged，进行数据更新
                         * ，这样可避免每次加载更多数据的时候，都会重新回到第一页。
                         */

                        if(totalPage==0)
                            Toast.makeText(InfoListViewActivity.this, "没有数据...", Toast.LENGTH_SHORT).show();

                        if (pageNo == 1) {
                            mListView.setAdapter(mAdapter);
                        }
                        //刷新
                        mAdapter.notifyDataSetChanged();

                        //记录当前参数
                        curTotalCount = totalCount;
                        curTotalPage = totalPage;

                        //刷新资料总数
                        appContext.setInfoSum((long) totalCount);

                        //防止pageNo大于总页数
                        pageNo++;
                        //隐藏提示
                        footerView.setVisibility(View.GONE);
                        //关闭动态提示
                        if(dialog!=null)
                            dialog.dismiss();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }, nrc);
    }
    private  void initListview() {
        pageNo = 1;
        datas = new ArrayList<Map<String,String>>();  //用来存放获取的所有数据
        datasend = new ArrayList<Info>();

        mAdapter = new ScrollAdapter(InfoListViewActivity.this, datas, R.layout.common_item_infolistview//R.layout.item
                , cols
                , new int[] { R.id.item_titlev
                , R.id.item_datav1
                , R.id.item_datav2
                , R.id.item_datav3
                , R.id.item_datav4
                , R.id.item_datav5
                , R.id.item_datav6
                , R.id.item_datav7
                , R.id.item_datav8
                , R.id.item_datav9});

        Log.d("InfoList","start loading");
        loading();//通过多线程获取数据并载入listview
    }
	public void addHViews(final InfoScrollView2 hScrollView) {
		if(!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
            InfoScrollView2 scrollView = mHScrollViews.get(size - 1);
			final int scrollX = scrollView.getScrollX();
			//第一次满屏后，向下滑动，有一条数据在开始时未加入
			if(scrollX != 0) {
				mListView.post(new Runnable() {
					@Override
					public void run() {
						//当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		mHScrollViews.add(hScrollView);
	}

	public void onScrollChanged(int l, int t, int oldl, int oldt){
		for(InfoScrollView2 scrollView : mHScrollViews) {
			//防止重复滑动
			if(mTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}

	class ScrollAdapter extends SimpleAdapter {

		private List<? extends Map<String, ?>> datas;
		private int res;
		private String[] from;
		private int[] to;
		private Context context;
		public ScrollAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.context = context;
			this.datas = data;
			this.res = resource;
			this.from = from;
			this.to = to;
		}
        public void bindData(List<? extends Map<String, ?>> data) {

            this.datas = data;
        }
        @Override
        public int getCount() {
//            Log.d("BarcloudsDebug:","getCount="+datas.size());
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
//            Log.d("BarcloudsDebug:","getView");
			if(v == null) {
				v = LayoutInflater.from(context).inflate(res, null);
				//第一次初始化的时候装进来
				addHViews((InfoScrollView2) v.findViewById(R.id.item_chscroll_scroll));
				View[] views = new View[to.length];
				//单元格点击事件
				for(int i = 0; i < to.length; i++) {
					View tv = v.findViewById(to[i]);
					tv.setOnClickListener(clickListener);
					views[i] = tv;
				}
				//每行点击事件
				/*for(int i = 0 ; i < from.length; i++) {
					View tv = v.findViewById(row_hlistview[i]);
				}*/
				//
				v.setTag(views);
			}
			View[] holders = (View[]) v.getTag();
			int len = holders.length;
//            Log.d("BarcloudsDebug:","size="+datas.size());
			for(int i = 0 ; i < len; i++) {
				((TextView)holders[i]).setText(this.datas.get(position).get(from[i]).toString());
			}
			return v;
		}
	}

	//测试点击的事件
	protected OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setBackgroundResource(R.drawable.linearlayout_green_round_selector);
			Toast.makeText(InfoListViewActivity.this, "点击了:" + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
		}
	};

    public void onClick(View v) {
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-start");
        switch (v.getId()) {
            case R.id.imagebutton_setting:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.imagebutton_search:
                Intent intent0 = new Intent(InfoListViewActivity.this,
                        SearchActivity.class);//SUGOOD
                startActivityForResult(intent0, 0);//此处的0为一个依据，可以写其他的值，但一定要>=0
                break;
            case R.id.imagebutton_refresh:
                selectFlag="page";//将标志设置为page，表示正在浏览数据；设置为search，表示正在搜索操作
                dialog =new CustomProgressDialog(InfoListViewActivity.this, "正在刷新...",R.anim.frame);
                dialog.show();
                initListview();
                break;
            case R.id.imagebutton_delete:
                Dialog dialogChoose=new AlertDialog.Builder(InfoListViewActivity.this)
                        .setTitle("删除全部数据")
                        .setIcon(R.mipmap.appicon)
                        .setMessage("确认删除吗？")
                                //相当于点击确认按钮
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogChoose, int which) {
                                dialog =new CustomProgressDialog(InfoListViewActivity.this, "正在删除...",R.anim.frame);
                                dialog.show();
                                jsondelete(0);
                            }
                        })
                                //相当于点击取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        })
                        .create();
                dialogChoose.show();
                break;
            default:
                break;
        }
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-end");
    }

    private void jsondelete(int id){
        NetRequestConstant nrc = new NetRequestConstant();
        // post请求
        nrc.setType(HttpRequestType.POST);
        NetRequestConstant.requestUrl = NetUrlConstant.INFODELETE;
        NetRequestConstant.context = this;
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("uid", user.getUsername());
        map.put("flag", ""+1);
        map.put("id", ""+id);
        NetRequestConstant.map = map;

        getServer(new Netcallback() {

            public void preccess(Object res, boolean flag) {
                if (res != null) {
                    try {
                        JSONObject object = new JSONObject((String) res);
                        String success = object.optString("success");
                        if (success.equals("1")) {
                            Toast.makeText(InfoListViewActivity.this, "删除数据成功！",
                                    Toast.LENGTH_LONG).show();
                            //刷新显示
                            initListview();
                        }else {
                            Toast.makeText(InfoListViewActivity.this, "删除数据失败",
                                    Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }, nrc);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String searchChar = data.getStringExtra("searchchar");
            if(searchChar!=null) {
                field0 = searchChar;
                selectFlag="search";//将标志设置为page，表示正在浏览数据；设置为search，表示正在搜索操作
                initListview();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

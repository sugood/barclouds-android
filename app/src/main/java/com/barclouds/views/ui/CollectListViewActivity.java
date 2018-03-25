package com.barclouds.views.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.adapter.DiaryMenuAdapter;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.bean.Data;
import com.barclouds.views.bean.Info;
import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.domain.InfoIndex;
import com.barclouds.views.domain.User;
import com.barclouds.views.interfaces.Netcallback;
import com.barclouds.views.myview.MyGridView;
import com.barclouds.views.zxing.CaptureActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 带滑动表头与固定列的ListView
 */
public class CollectListViewActivity extends BaseActivity implements
         OnClickListener{
    private MyGridView gridview_menu;
    private DiaryMenuAdapter adapter;
    private ImageButton imagebutton_setting;

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
    protected List<CollectScrollView2> mHScrollViews =new ArrayList<CollectScrollView2>();
    private String[] cols = new String[] { "title", "data_1", "data_2", "data_3", "data_4", "data_5",
         "data_6","data_7","data_8", "data_9",};
    private  ScrollAdapter mAdapter;

    private boolean is_firstShow=true;// 是否进行分页操作
    private static int pageNo = 1;//设置pageNo的初始化值为1，即默认获取的是第一页的数据。

    private List<Map<String, String>> datas;  //用来存放获取的所有数据

    public List<Info> datasend;
//    List<Map<String, String>> datas = new ArrayList<Map<String,String>>();

    // gridView数据源
    private int[] i = new int[] { R.mipmap.diary_layout_auto,
            R.mipmap.diary_layout_scan, R.mipmap.diary_layout_layout };

    private String[] names = new String[]{"手动输入","手机扫描","其他功能"};

    private static final int REQUEST_CODE_SCAN = 1;   //请求码
    private static final int REQUEST_CODE_INPUT = 2;   //请求码

    long exitTime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_hlistview);
        Log.d("BarcloudsDebug", "CollectListViewActivity.onCreate");
		//init();
	}

	void init() {
//        Log.d("BarcloudsDebug","init");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collectlistview);

        gridview_menu = (MyGridView) findViewById(R.id.gridview_menu);
        adapter = new DiaryMenuAdapter(this, i,names);
        gridview_menu.setAdapter(adapter);

//		List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
//		Map<String, String> data = null;

        //获取当前用户数据列名
        AppContext appContext = (AppContext) getApplicationContext();
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

        imagebutton_setting.setOnClickListener(this);

        CollectScrollView2 headerScroll = (CollectScrollView2) findViewById(R.id.item_scroll_title);
		//添加头滑动事件
		mHScrollViews.add(headerScroll);
		mListView = (ListView) findViewById(R.id.hlistview_scroll_list);

        initListview();//初始化ListView数据
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                /**
//                 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=总页数时，加载更多数据。
//                 */
////                Log.d("BarCloudsDebug:", "totalPage=" + totalPage);
////                Log.d("BarCloudsDebug:", "pageNo=" + pageNo);
////                Log.d("BarCloudsDebug:", "is_divPage=" + is_divPage);
//                if (is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//                    if (pageNo <= totalPage){
//                        loading();
//                        footerView.setVisibility(View.VISIBLE);
//                    } else if (pageNo > totalPage ) {
//                        footerView.setVisibility(View.GONE);
//                        Toast.makeText(CollectListViewActivity.this, "没有更多数据...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            /**
//             * 当：第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=所有的item总数的时候，
//             * is_divPage变为TRUE，这个时候才会加载数据。
//             */
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
////                Log.d("BarCloudsDebug:","totalItemCount="+totalItemCount);
////                Log.d("BarCloudsDebug:","firstVisibleItem="+firstVisibleItem);
////                Log.d("BarCloudsDebug:","visibleItemCount="+visibleItemCount);
//            }
//        });
        gridview_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                switch (position) {

                    case 0:// 手动输入
                        Intent intent0 = new Intent(CollectListViewActivity.this,
                                InputActivity.class);//SUGOOD

                        startActivityForResult(intent0, REQUEST_CODE_INPUT);//此处的0为一个依据，可以写其他的值，但一定要>=0
//					startActivity(intent0);
                        Log.d("BarcloudsDebug","启动扫描");
                        break;
                    case 1:// 手机扫描
                        Intent intent1 = new Intent(CollectListViewActivity.this,
                                CaptureActivity.class);//SUGOOD

                        startActivityForResult(intent1, REQUEST_CODE_SCAN);//此处的0为一个依据，可以写其他的值，但一定要>=0
//					startActivity(intent0);
                        Log.d("BarcloudsDebug","启动扫描");
                        break;
                    case 2:// 自动生成日记
//                        Intent intent2 = new Intent(CollectListViewActivity.this,
//                                MovieActivity.class);//SUGOOD
//                        intent2.putExtra("flag", 5);
//                        startActivity(intent2);
                        break;

                    default:
                        break;
                }
            }
        });
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null) {
            if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SCAN) {
                Bundle b = data.getExtras(); //data为B中回传的Intent
                String str = b.getString("str1");//str即为回传的值
                loading(str);//通过多线程获取数据并载入listview
            } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_INPUT) {

                String searchChar = data.getStringExtra("searchchar");
                if (searchChar != null) {
                    loading(searchChar);
                }
            }
        }
//        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
//            case resultCode == RESULT_OK && requestCode == REQUEST_CODE_IN:
//                Bundle b=data.getExtras(); //data为B中回传的Intent
//                String str=b.getString("str1");//str即为回传的值
//
//
//                loading(str);//通过多线程获取数据并载入listview
//                break;
//            default:
//                break;
//        }
    }

    public void loading(String barcode) {
//        footerView.setVisibility(View.VISIBLE);
        NetRequestConstant nrc = new NetRequestConstant();
        // post请求
        nrc.setType(HttpRequestType.POST);
        NetRequestConstant.requestUrl = NetUrlConstant.COLLECTDATA;
        NetRequestConstant.context = this;
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("uid", user.getUsername());
        map.put("field0", ""+barcode);
        NetRequestConstant.map = map;

        getServer(new Netcallback() {

            public void preccess(Object res, boolean flag) {
                if (res != null) {
                    try {
                        Map<String, String> data = null;
                        Gson gson = new Gson();
//                        Log.d("BarcloudsDebug","showhtml"+(String) res);
                        Data collectdata = gson.fromJson((String) res, Data.class);

                        data = new HashMap<String, String>();
                        data.put("title", ""+collectdata.getField0());
                        data.put("data_1",""+collectdata.getField1());
                        data.put("data_2",""+collectdata.getField2());
                        data.put("data_3",""+ collectdata.getField3());
                        data.put("data_4",""+collectdata.getField4());
                        data.put("data_5",""+collectdata.getField5());
                        data.put("data_6",""+collectdata.getField6());
                        data.put("data_7",""+collectdata.getField7());
                        data.put("data_8",""+collectdata.getField8());
                        data.put("data_9",""+collectdata.getField9());
                        datas.add(data);

//                        Log.d("BarcloudsDebug:","datassize="+datas.size());
                        mAdapter.bindData(datas);
                        /**
                         * 当pageNo等于1的时候才会setAdapter，以后不会再设置，直接notifyDataSetChanged，进行数据更新
                         * ，这样可避免每次加载更多数据的时候，都会重新回到第一页。
                         */
                        Log.d("BarcloudsDebug:","is_firstShow="+is_firstShow);
                        if (is_firstShow == true) {
                            mListView.setAdapter(mAdapter);
                            is_firstShow = false;
                        }
                        mAdapter.notifyDataSetChanged();

//                        footerView.setVisibility(View.GONE);
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

        mAdapter = new ScrollAdapter(CollectListViewActivity.this, datas, R.layout.common_item_collectlistview//R.layout.item
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
    }
	public void addHViews(final CollectScrollView2 hScrollView) {
		if(!mHScrollViews.isEmpty()) {
			int size = mHScrollViews.size();
            CollectScrollView2 scrollView = mHScrollViews.get(size - 1);
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
		for(CollectScrollView2 scrollView : mHScrollViews) {
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
				addHViews((CollectScrollView2) v.findViewById(R.id.item_chscroll_scroll));
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
			Toast.makeText(CollectListViewActivity.this, "点击了:" + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
		}
	};

    public void onClick(View v) {
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-start");
        switch (v.getId()) {
            case R.id.imagebutton_setting:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.imagebutton_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
//            case R.id.imagebutton_sort:
////                initListview();
//                break;
            default:
                break;
        }
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-end");
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

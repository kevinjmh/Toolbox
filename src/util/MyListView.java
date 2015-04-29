package util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Manhua on 2014/6/10.
 * ScrollView 嵌套 ListView 只显示了一行，有一种重设ListView高度的方法，但还会遮住一部分，
 * 于是采用一个自定义的组件继承于原来的类，重写onMeasure方法会重绘组件。然后把ListView布局换成自定义的即可解决
 */
public class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

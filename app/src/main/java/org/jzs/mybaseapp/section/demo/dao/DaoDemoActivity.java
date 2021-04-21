package org.jzs.mybaseapp.section.demo.dao;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jzs.mybaseapp.greendao.DaoMaster;
import com.jzs.mybaseapp.greendao.DaoSession;
import com.jzs.mybaseapp.greendao.UserDao;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.databinding.ActivityDaoDemoBinding;

import java.util.List;

/**
 * @author Jzs created 2017/8/2
 */

public class DaoDemoActivity extends AppCompatActivity {

    private ActivityDaoDemoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dao_demo);
        initView();
    }


    private void initView() {
        //创建dbmanager类初始化数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "user1.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();

// 获取userDao
        UserDao userDao = daoSession.getUserDao();


        mBinding.btnAdd.setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                User mUser = new User(null, i + "", "yuShu" + i, i);
                userDao.insert(mUser);
            }
        });
        mBinding.btnAdd2.setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                User mUser = new User(null, i + "", "yuShu" + i, i);
                userDao.insert(mUser);
            }
        });
        mBinding.btnDel.setOnClickListener(v -> {
            userDao.deleteAll();
            //条件删除
            userDao.queryBuilder().where(UserDao.Properties.Uid.eq(1)).buildDelete().executeDeleteWithoutDetachingEntities();
        });
        mBinding.btnUpdate.setOnClickListener(v -> {
            try {
                User mUser = new User((long) 2, "9", "改改改", 25);
                userDao.update(mUser);
            } catch (Exception e) {
                Toast.makeText(this, "未找到数据", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
        mBinding.btnQuery.setOnClickListener(v -> {
            mBinding.tvContent.setText("");
            final List<User> users = userDao.loadAll();//查询所有
            for (int i = 0; i < users.size(); i++) {
                mBinding.tvContent.setText(mBinding.tvContent.getText() + users.get(i).toString());
            }
        });
    }
}

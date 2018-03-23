package com.left.elevator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.left.elevator.bean.User;
import com.left.elevator.views.MainFragment;
import com.left.elevator.views.OthersFragment;
import com.left.elevator.views.SettingFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FragmentManager fm;
    private MainFragment mainFragment;
    private OthersFragment othersFragment;
    private SettingFragment settingFragment;
    private CircleImageView imageView;
    private TextView username;
    private TextView email;
    private User user;
    private Bitmap head;//头像Bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();
        // 设置默认的Fragment
        setDefaultFragment();

        fab= (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);

        user = BmobUser.getCurrentUser(this, User.class);
        if (user != null) {
            //设置界面用户信息
            username.setText(user.getUsername());
            email.setText(user.getEmail());

            BmobQuery bmobQuery = new BmobQuery();
            bmobQuery.getObject(getApplicationContext(), user.getObjectId(), new GetListener<User>() {
                @Override
                public void onSuccess(User o) {

                    //显示图片的配置
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();
                    //载入图片
                    ImageLoader.getInstance().displayImage(o.getHead().getFileUrl(getApplicationContext()), imageView, options);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });


        } else {
            //缓存用户对象为空时， 打开登录界面
            Snackbar.make(navigationView, getString(R.string.userinfo_overdue), Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册里面取照片
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (mainFragment == null) {
                mainFragment = new MainFragment();
            }
            showFragment(mainFragment, R.string.title_main);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_gallery) {
            if (othersFragment == null) {
                othersFragment = new OthersFragment();
            }
            showFragment(othersFragment, R.string.title_others);
            fab.setVisibility(View.INVISIBLE);
        }  else if (id == R.id.nav_manage) {
            if (settingFragment == null) {
                settingFragment = new SettingFragment();
            }
            showFragment(settingFragment, R.string.title_setting);
            fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, "分享文字");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        }  else if (id == R.id.nav_logout) {
            User.logOut(getApplicationContext());   //清除缓存用户对象
            //跳转至登录页
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        final BmobFile bmobFile = new BmobFile(saveBitmap2file(head));
                        bmobFile.uploadblock(getApplicationContext(), new UploadFileListener() {

                            @Override
                            public void onSuccess() {
                                //记得更新对应user的头像
                                User newUser = new User();
                                newUser.setHead(bmobFile);
                                newUser.update(getApplicationContext(), user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        //用ImageView显示出来
                                        imageView.setImageBitmap(head);
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        Snackbar.make(navigationView, getString(R.string.error_head_replace), Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(Integer value) {

                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                Snackbar.make(navigationView, getString(R.string.error_head_replace), Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }


    public File saveBitmap2file(Bitmap bmp) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File out = new File(Environment.getExternalStorageDirectory(), "/" + user.getUsername() + "_head.jpg");
        if (!out.exists()) {
            try {
                out.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            stream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return out;
    }


    /**
     * 设置activity一启动显示的默认content
     */
    private void setDefaultFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        mainFragment = new MainFragment();
        transaction.add(R.id.content, mainFragment);
        transaction.commit();
        //设置左上角标题
        setTitle(R.string.title_main);
    }

    /**
     * 起切换fragment的作用
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment, int titleId) {
        setTitle(titleId);

        FragmentTransaction transaction = fm.beginTransaction();
        //先判断当前的有没有，有的话直接做显示，没的话先添加
        if (!fm.getFragments().contains(fragment)) {
            transaction.add(R.id.content, fragment);
        }
        //先全部hide
        if (fm.getFragments().contains(mainFragment)) {
            transaction.hide(mainFragment);
        }
        if (fm.getFragments().contains(othersFragment)) {
            transaction.hide(othersFragment);
        }
        if (fm.getFragments().contains(settingFragment)) {
            transaction.hide(settingFragment);
        }
        //显示要显示的
        transaction.show(fragment);
        transaction.commit();
    }
}

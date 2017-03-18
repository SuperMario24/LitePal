package com.example.saber.litepaltest;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateData;
    private Button btnAddData;
    private Button btnUpdateData;
    private Button btnDeleteData;
    private Button btnQueryData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreateData = (Button) findViewById(R.id.btn_create_database);
        btnAddData = (Button)findViewById(R.id.btn_add_data);
        btnUpdateData = (Button) findViewById(R.id.btn_update_data);
        btnDeleteData = (Button) findViewById(R.id.btn_delete_data);
        btnQueryData = (Button)findViewById(R.id.btn_query_data);


        /**
         * 创建数据库，表
         */
        btnCreateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建数据库
                Connector.getDatabase();
            }
        });

        /**
         * 插入数据
         */
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unkown");
                //插入数据的方法 save()
                book.save();
            }
        });


        /**
         * 更新数据
         */
        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setPrice(14.95);
                book.setPress("Author");
                //更新数据
                book.updateAll("name = ? and author = ?","The Lost Symbol","Dan Brown");


                //更新为默认值的方法
                //book.setToDefault("pages");
                //updateAll()没有约束语句时，对所有记录生效
                //book.updateAll();

            }
        });


        /**
         * 删除数据
         */
        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //第一个参数为表名，第二三个参数为约束条件
                DataSupport.deleteAll(Book.class,"price < ?","15");
            }
        });


        /**
         * 查询数据
         */
        btnQueryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> books = DataSupport.findAll(Book.class);
                for(Book book : books){
                    Log.d("info","book name is "+book.getName());
                    Log.d("info","book author is "+book.getAuthor());
                    Log.d("info","book pages is "+book.getPages());
                    Log.d("info","book price is "+book.getPrice());
                    Log.d("info","book press is "+book.getPress());
                }

                //查询第一条数据
                Book firstBook = DataSupport.findFirst(Book.class);
                //查询最后一条数据
                Book lastBook = DataSupport.findLast(Book.class);
                //只查询两列的数据
                List<Book> books1 = DataSupport.select("name","author").find(Book.class);
                //只查询页数大于400的数据
                List<Book> books2 = DataSupport.where("pages > ?","400").find(Book.class);
                //价格降序排序
                List<Book> books3 = DataSupport.order("price desc").find(Book.class);
                //只查询表中前三条数据
                List<Book> books4 = DataSupport.limit(3).find(Book.class);

                //查询2,3,4条数据，可以查到具体的第几条数据
                List<Book> books5 = DataSupport.limit(3).offset(1).find(Book.class);

                //复合查询
                List<Book> books6 = DataSupport.select("name","author")
                        .where("pages > ?","400")
                        .order("pages")
                        .limit(1)
                        .offset(1)
                        .find(Book.class);

                //原生sql
                Cursor c = DataSupport.findBySQL("select * from Book where pages > ? and price < ?","400","20");

            }
        });


    }
}

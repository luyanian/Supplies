package cn.soft_x.supplies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.model.MessageDetailModel;
import cn.soft_x.supplies.model.MessageModel;
import cn.soft_x.supplies.model.nonelectrical.MessageModel1;
import cn.soft_x.supplies.utils.Constant;

/**
 * Created by Administrator on 2017-01-11.
 */
public class MyDBControl {
    private MySQLiteOpenHelper mHelper;
    private SQLiteDatabase mDb;

    public MyDBControl(Context context) {
        mHelper = new MySQLiteOpenHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    public void close() {
        mDb.close();
    }

    /**
     * 查询该用户在该表中是否有数据
     *
     * @param tableName
     * @return
     */
    public boolean isTableNull(String tableName) {
        Cursor cursor = mDb.rawQuery("select * from " + tableName + " where "
                + MySQLiteOpenHelper.USER_ID + "=?", new String[]{Constant.USER_ID});
        boolean isNull = cursor.getCount() == 0;
        cursor.close();
        return isNull;
    }
    /**
     * 消息类型 (
     * 1:采购信息 [
     *      1:拾起卖消息，2:调货商，3:拆解企业，4:供货商消息]
     * 2:订单消息[
     *      1.供货消息，2.修改预计到厂时间3.到厂时间，4.验质消息，5结算消息，6,异常消息,
     *      7,发车消息,8发布供货消息]
     * 3:系统消息[
     *      1，修改密码，2修改手机号]
     * )
     */
    /**
     * 查询消息详情
     *
     * @param msgDLType
     * @param msgLXType
     * @param page
     * @return
     */
    public synchronized List<MessageDetailModel.ListBean> selectMsgDetailTable(String msgDLType, String msgLXType, int page) {
        List<MessageDetailModel.ListBean> list = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME
                + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX + "=? order by "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME + " desc limit " + 10 * page + ",10", new String[]{Constant.USER_ID, msgDLType, msgLXType});
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                //            String GLCOMPANYID, String TIME, int XXLX, int XXDL, String CONTENT, String GLID
                int _id = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_ID));
                int xxdl = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL));
                String GLCOMPANYID = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_COMPANYID));
                int xxlx = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX));
                String content = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_CONTENT));
                String glid = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_GLID));
                // 已读 未读
                int read = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_READ));
                long time = cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME));
                //                                                                      timeStr ↓
                MessageDetailModel.ListBean b = new MessageDetailModel.ListBean(GLCOMPANYID, time, xxlx, xxdl, content, glid, read);
                list.add(b);
                Logger.d("已读状态->%d_id->%d", read, _id);
                Logger.d(b.toString());
            }
            Logger.i("本次数据库查询到的数据条数->%d", list.size());
        } else {
            ToastUtil.showToast(SuppliesApplication.getAppContext(), "没有更多内容!");
            Logger.i("本次数据库查询到的数据条数为零");
        }
        cursor.close();
        return list;
    }

    /**
     * 添加消息详情
     *
     * @param model
     */
    public synchronized void addMsgDetailTable(MessageDetailModel model) {
        if (model.getList().size() == 0) {
            Logger.i("本次操作无数据");
            return;
        }
        for (MessageDetailModel.ListBean b : model.getList()) {
            Logger.i("toString->" + b.toString());
            ContentValues c = new ContentValues();
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_GLID, b.getGLID());
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL, b.getXXDL());
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX, b.getXXLX());
            c.put(MySQLiteOpenHelper.USER_ID, Constant.USER_ID);
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME, b.getTIME());
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_READ, 0);
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_CONTENT, b.getCONTENT());
            c.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_COMPANYID, b.getGLCOMPANYID());
            mDb.insert(MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME, null, c);
            Logger.e("加入数据的打印->" + b.toString());
            c.clear();
        }
        Logger.i("本次加入数据为->%d", model.getList().size());
    }

    /**
     * 点击设置为已读
     *
     * @param glid
     * @param msgdl
     * @param msglx
     */
    public synchronized void upDateForRead(String glid, int msgdl, int msglx) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.MSG_DETAIL_TABLE_READ, 1);
        int i = mDb.update(MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME, contentValues,
                MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_DETAIL_TABLE_GLID + "=? and "
                        + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, glid, msgdl + "", msglx + ""});

        Logger.i("更新已读操作->%d", i);
        contentValues.clear();
    }

    /**
     * 测试已读是否成功
     *
     * @param glid
     * @param msgdl
     * @param msglx
     */
    public synchronized void searchForReadTest(String glid, int msgdl, int msglx, String msg) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_GLID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX + "=? limit 0,1", new String[]{Constant.USER_ID, glid, msgdl + "", msglx + ""});
        while (cursor.moveToNext()) {
            String glids = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_GLID));
            int read = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_READ));
            int _id = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_ID));
            Logger.i("Test    查询已读状态消息为->%d" + "," + msg, read);
            Logger.i("Test glid->" + glid + ",glids->" + glids);
            Logger.i("Test _id->" + _id);
        }
        cursor.close();
    }

    /**
     * 通过子表查询外层去显示已读还是未读
     * <p/>
     * 要通过大类去循环小类出是否已读
     *
     * @param xxdl
     * @param xxlx
     * @return true 全部已读 false 某些未读
     */
    public synchronized boolean searchMsgRead(int xxdl, int xxlx) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_READ)) == 0) {
                    //                    upDateMsgRead(xxdl, xxlx, 1);
                    cursor.close();
                    return false;
                }
            }
            //            upDateMsgRead(xxdl, xxlx, 2);
            cursor.close();
            return true;
        }
    }

    /**
     * 从子表查询最新时间
     *
     * @param xxdl
     * @param xxlx
     * @return
     */
    public synchronized long searchMsgTime(int xxdl, int xxlx) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGLX + "=? order by "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME + " desc limit 0,1", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        if (cursor.getCount() == 0) {
            cursor.close();
            return -1;
        } else {
            while (cursor.moveToNext()) {
                //                upDateMsgTime(xxdl, xxlx, cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME)));
                return cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME));
            }
            cursor.close();
            return -1;
        }
    }

    public synchronized long searchMsgTime(int xxdl) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_MSGDL + "=? order by "
                + MySQLiteOpenHelper.MSG_DETAIL_TABLE_TIME + " desc limit 0,1", new String[]{Constant.USER_ID, xxdl + ""});
        if (cursor.getCount() == 0) {
            return -1;
        } else {
            while (cursor.moveToNext()){
                return cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME));
            }
            return -1;
        }

    }

    /**
     * 更新外层已读状态
     *
     * @param xxdl
     * @param xxlx
     * @param read
     */
    public synchronized void upDateMsgRead(int xxdl, int xxlx, int read) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.MSG_TABLE_READ, read);
        mDb.update(MySQLiteOpenHelper.MSG_TABLE_NAME, contentValues,
                MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        contentValues.clear();
    }

    /**
     * 更新外层时间
     *
     * @param xxdl
     * @param xxlx
     * @param time
     */
    public void upDateMsgTime(int xxdl, int xxlx, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME, time);
        mDb.update(MySQLiteOpenHelper.MSG_TABLE_NAME, contentValues,
                MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        contentValues.clear();
    }


    /**
     * 查询外层显示，时间倒排？
     *
     * @param xxdl
     * @return
     */
    public synchronized List<MessageModel.ListBean> orderMsgByTime(int xxdl) {
        List<MessageModel.ListBean> list = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + MySQLiteOpenHelper.MSG_TABLE_NAME + " where "
                        + MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGDL + "=?"
                , new String[]{Constant.USER_ID, xxdl + ""});
        while (cursor.moveToNext()) {
            int xxlx = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_TABLE_MSGLX));
            String content = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_TABLE_MSG_CONTENT));
            int read = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_TABLE_READ));
            long time = cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME));
            MessageModel.ListBean bean = new MessageModel.ListBean(read, time, xxlx, xxdl, content);
            list.add(bean);
        }
        cursor.close();
        if (list.size() == 0) {
            ToastUtil.showToast(SuppliesApplication.getAppContext(), "没有更多内容!");
        }
        Logger.i("外层查询结果为->%d", list.size());
        return list;
    }

    public synchronized boolean searchMsg(int xxdl, int xxlx) {
        Cursor cursor = mDb.rawQuery("select * from " + MySQLiteOpenHelper.MSG_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        return cursor.getCount() == 0;
    }

    /**
     * 添加或者更新外层表数据
     *
     * @param model
     */
    public synchronized void addOrUpDateMsgTable(MessageModel model) {
        if (model.getList().size() == 0) {
            Logger.i("外层无数据可以添加");
            return;
        }
        for (MessageModel.ListBean b : model.getList()) {
            if (b.getXXDL() == 3) {
                Logger.i("addOrUpDateMsgTable 插入系统消息");
                ContentValues contentValues = new ContentValues();
                contentValues.put(MySQLiteOpenHelper.USER_ID, Constant.USER_ID);
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_READ, 1);
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_CONTENT, b.getCONTENT());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGDL, b.getXXDL());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGLX, b.getXXLX());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME, b.getTIME());
                mDb.insert(MySQLiteOpenHelper.MSG_TABLE_NAME, null, contentValues);
                contentValues.clear();
            } else {
                boolean isNotHave = searchMsg(b.getXXDL(), b.getXXLX());
                // 没有这条就插入新的
                Logger.i("addOrUpDateMsgTable  time->" + isNotHave);
                if (isNotHave) {
                    Logger.i("addOrUpDateMsgTable 插入");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MySQLiteOpenHelper.USER_ID, Constant.USER_ID);
                    contentValues.put(MySQLiteOpenHelper.MSG_TABLE_READ, 0);
                    contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_CONTENT, b.getCONTENT());
                    contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGDL, b.getXXDL());
                    contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGLX, b.getXXLX());
                    contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME, b.getTIME());
                    mDb.insert(MySQLiteOpenHelper.MSG_TABLE_NAME, null, contentValues);
                    contentValues.clear();
                } else {
                    // 如果有就做更新
                    boolean read = searchMsgRead(b.getXXDL(), b.getXXLX());
                    Logger.i("addOrUpDateMsgTable 更新  read->" + read);
                    upDateMsgTime(b.getXXDL(), b.getXXLX(), b.getTIME());
                    if (read)
                        upDateMsgRead(b.getXXDL(), b.getXXLX(), 1);
                    else
                        upDateMsgRead(b.getXXDL(), b.getXXLX(), 0);
                }
            }
        }
    }


    public void upDateMSG(int xxdl, int xxlx) {
        long time = searchMsgTime(xxdl, xxlx);
        if (time != -1) {
            upDateMsgTime(xxdl, xxlx, time);
        }
        if (searchMsgRead(xxdl, xxlx)) {
            upDateMsgRead(xxdl, xxlx, 1);
        } else {
            upDateMsgRead(xxdl, xxlx, 0);
        }
    }

    public void dropMsg(int xxdl, int xxlx) {
        mDb.delete(MySQLiteOpenHelper.MSG_TABLE_NAME,
                MySQLiteOpenHelper.MSG_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_TABLE_MSGLX + "=?", new String[]{xxdl + "", xxlx + ""});
    }


    /**
     * *****************************************供货商消息数据处理********************************************
     */

    public synchronized boolean searchGhsMsgRead(int xxdl, int xxlx,String gliid) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "",gliid, xxlx + ""});
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_READ)) == 0) {
                    cursor.close();
                    return false;
                }
            }
            cursor.close();
            return true;
        }
    }
    public synchronized long searchGhsMsgTime(int xxdl) {
        Cursor cursor = mDb.rawQuery("select * from "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? order by "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME + " desc limit 0,1", new String[]{Constant.USER_ID, xxdl + ""});
        if (cursor.getCount() == 0) {
            return -1;
        } else {
            while (cursor.moveToNext()){
                return cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME));
            }
            return -1;
        }

    }

    public synchronized long searchGhsMsgTime(int xxdl,int xxlx) {
        String sql = "select * from "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX + "=? order by "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME + " desc limit 0,1";
        Cursor cursor = mDb.rawQuery(sql, new String[]{Constant.USER_ID, xxdl + "",xxlx+""});
        if (cursor.getCount() == 0) {
            return -1;
        } else {
            while (cursor.moveToNext()){
                return cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME));
            }
            return -1;
        }

    }

    /**
     * 更新外层已读状态
     *
     * @param xxdl
     * @param xxlx
     * @param read
     */
    public synchronized void upDateGhsMsgRead(int xxdl, int xxlx,String gliId, int read) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_READ, read);
        mDb.update(MySQLiteOpenHelper.MSG_GHS_TABLE_NAME, contentValues,
                MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLID + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "",gliId, xxlx + ""});
        contentValues.clear();
    }

    /**
     * 更新外层时间
     *
     * @param xxdl
     * @param xxlx
     * @param time
     */
    public void upDateGhsMsgTime(int xxdl, int xxlx, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME, time);
        mDb.update(MySQLiteOpenHelper.MSG_GHS_TABLE_NAME, contentValues,
                MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "", xxlx + ""});
        contentValues.clear();
    }


    /**
     * 查询外层显示，时间倒排？
     *
     * @param xxdl
     * @return
     */
    public synchronized List<MessageModel1.ListBean> orderGhsMsgByTime(int xxdl) {
        List<MessageModel1.ListBean> list = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + MySQLiteOpenHelper.MSG_GHS_TABLE_NAME + " where "
                        + MySQLiteOpenHelper.USER_ID + "=? and "
                        + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=?"
                , new String[]{Constant.USER_ID, xxdl + ""});
        while (cursor.moveToNext()) {
            int xxlx = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX));
            String content = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_CONTENT));
            int read = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_READ));
            long time = cursor.getLong(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_TIME));
            String ywtags = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_YWTAGS));
            String glid = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLID));
            String glcompanyid = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLCOMPANYID));
            MessageModel1.ListBean bean = new MessageModel1.ListBean(time, xxlx, xxdl, content,ywtags,glid,glcompanyid,read);
            list.add(bean);
        }
        cursor.close();
        if (list.size() == 0) {
            ToastUtil.showToast(SuppliesApplication.getAppContext(), "暂无数据!");
        }
        Logger.i("外层查询结果为->%d", list.size());
        return list;
    }

    public synchronized boolean searchGhsMsg(int xxdl, int xxlx, String glid) {
        Cursor cursor = mDb.rawQuery("select * from " + MySQLiteOpenHelper.MSG_GHS_TABLE_NAME + " where "
                + MySQLiteOpenHelper.USER_ID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGDL + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLID + "=? and "
                + MySQLiteOpenHelper.MSG_GHS_TABLE_MSGLX + "=?", new String[]{Constant.USER_ID, xxdl + "",glid, xxlx + ""});
        return cursor.getCount() == 0;
    }

    /**
     * 添加或者更新外层表数据
     *
     * @param model
     */
    public synchronized void addOrUpDateGhsMsgTable(MessageModel1 model) {
        if (model.getList().size() == 0) {
            Logger.i("外层无数据可以添加");
            return;
        }
        for (MessageModel1.ListBean b : model.getList()) {
            boolean isNotHave = searchGhsMsg(b.getXxdl(), b.getXxlx(),b.getGlid());
            // 没有这条就插入新的
            Logger.i("addOrUpDateMsgTable  time->" + isNotHave);
            if (isNotHave) {
                Logger.i("addOrUpDateMsgTable 插入");
                ContentValues contentValues = new ContentValues();
                contentValues.put(MySQLiteOpenHelper.USER_ID, Constant.USER_ID);
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_READ, 0);
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_CONTENT, b.getContent());
                contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_YWTAGS, b.getYwtags());
                contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLID, b.getGlid());
                contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_GLCOMPANYID, b.getGlcompanyid());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGDL, b.getXxdl());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSGLX, b.getXxlx());
                contentValues.put(MySQLiteOpenHelper.MSG_TABLE_MSG_TIME, b.getTime());
                contentValues.put(MySQLiteOpenHelper.MSG_GHS_TABLE_MSG_UPDATE_TIME, System.currentTimeMillis()/1000);
                long ret = mDb.insert(MySQLiteOpenHelper.MSG_GHS_TABLE_NAME, null, contentValues);
                contentValues.clear();
            } else {
                // 如果有就做更新
                boolean read = searchGhsMsgRead(b.getXxdl(), b.getXxlx(),b.getGlid());
                Logger.i("addOrUpDateMsgTable 更新  read->" + read);
                upDateGhsMsgTime(b.getXxdl(), b.getXxlx(),System.currentTimeMillis()/1000);
//                    if (read) {
//                        upDateGhsMsgRead(b.getXxdl(), b.getXxlx(), 0);
//                    }else {
//                        upDateGhsMsgRead(b.getXxdl(), b.getXxlx(), 1);
//                    }
            }
        }
    }


    public void upDateGhsMSG(int xxdl, int xxlx,String gliId) {
        long time = searchGhsMsgTime(xxdl, xxlx);
        if (time != -1) {
            upDateGhsMsgTime(xxdl, xxlx, time);
        }
        if (searchGhsMsgRead(xxdl, xxlx,gliId)) {
            upDateGhsMsgRead(xxdl, xxlx,gliId, 1);
        } else {
            upDateGhsMsgRead(xxdl, xxlx,gliId, 0);
        }
    }


    /**
     * 时间转换
     *
     * @param time
     * @return
     */
    private String getTimeStr(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String timeStr = formatter.format(date);
        return timeStr;
    }

}

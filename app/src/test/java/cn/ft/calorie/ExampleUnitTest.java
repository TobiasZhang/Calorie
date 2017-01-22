package cn.ft.calorie;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.util.TakePictureUtils;
import cn.ft.calorie.util.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void upload(){
        ApiUtils apiUtils = ApiUtils.getInstance();
        Map<String,String> options = new HashMap<>();
        options.put("userId","588238acf978e73df00087c0");
        options.put("startDate",new Date((2017-1900),0,21).toGMTString());
        options.put("endDate",new Date((2017-1900),2,1).toGMTString());
        System.out.println("--in-----");
        apiUtils.getApiDataObservable(
                apiUtils.getApiServiceImpl().getIntakeRecords(options))
                .subscribe(records->{
                    System.out.println("--out-----");
                    for(IntakeRecord record:records){
                        System.out.println(record.getId()+"----id");
                    }
                    System.out.println(records.size()+"---------size");
                });
    }


}
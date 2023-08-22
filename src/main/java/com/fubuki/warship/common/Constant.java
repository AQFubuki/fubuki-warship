package com.fubuki.warship.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 描述：     常量值
 */
@Component
public class Constant {

    public static final String FUBUKI_WARSHIP_USER = "fubuki_warship_user";
    public static final String SALT = "i80j^*HBj07j23&&H,.'<";
    //静态变量不能直接注入，要用set方法注入
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ORDER_ENUM =
                Sets.newHashSet("price desc", "price asc");
    }
    public interface SaleStatus {

        int NOT_SALE = 0;//商品下架状态
        int SALE = 1;//商品上架状态
    }

    public interface Cart {

        Integer NOT_SELECTED = 0;//购物车未选中
        Integer SELECTED = 1;//购物车已选中
    }



//    public static String ICODE;
//
//    @Value("${icode}")
//    public void setICODE(String icode) {
//        ICODE = icode;
//    }
}

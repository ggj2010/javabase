package com.ggj.java.lettcode.array;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/maximum-product-of-three-numbers/
 * 给你一个整型数组 nums ，在数组中找出由三个数组成的最大乘积，并输出这个乘积。
 *
 * @author gaoguangjin
 */
public class MaximumProduct {





    /**
     * 首先将数组排序。
     * <p>
     * 如果数组中全是非负数，则排序后最大的三个数相乘即为最大乘积；如果全是非正数，则最大的三个数相乘同样也为最大乘积。
     * <p>
     * 如果数组中有正数有负数，则最大乘积既可能是三个最大正数的乘积，也可能是两个最小负数（即绝对值最大）与最大正数的乘积。
     * <p>
     * 综上，我们在给数组排序后，分别求出三个最大正数的乘积，以及两个最小负数与最大正数的乘积，二者之间的最大值即为所求答案。
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        int[] nums = {1, 5, 6};
        int n = nums.length;
        Arrays.sort(nums);
        Math.max(nums[0] * nums[1] * nums[n - 1], nums[n - 3] * nums[n - 2] * nums[n - 1]);

        Bean bean=JSONObject.parseObject(FileUtils.readFileToString(new File("/Users/gaoguangjin/usr/java/code/javabase/dailycode/src/main/java/com/ggj/java/lettcode/array/a.txt")),Bean.class);

        for (Bean.DataBean dataBean : bean.getData()) {
            String id=dataBean.getId();
            String name=dataBean.getName();
            for (Bean.DataBean.StatesBean state : dataBean.getStates()) {
                int code=state.getCode();
                String stateName=state.getName();
                for (Bean.DataBean.StatesBean.CitiesBean city : state.getCities()) {
                    int cityCode=city.getCode();
                    String cityName=city.getName();

                    for (Bean.DataBean.StatesBean.CitiesBean.DistrictsBean districtsBean : city.getDistricts()) {
                        String dictCode=districtsBean.getCode();
                        String dictName=districtsBean.getName();
                       // System.out.println(stateName);
                       // System.out.println(code);
                        //System.out.println(cityName);
                        //System.out.println(cityCode);
                       // System.out.println(dictCode);
                        System.out.println(dictName);
                    }
                }
            }
        }
    }

    @Data
    public static class Bean {
        private boolean status;
        private List<DataBean> data;

        @Data
        public static class DataBean {

            private String id;
            private String name;
            private List<StatesBean> states;


            public static class StatesBean {

                private String name;
                private int code;
                private List<CitiesBean> cities;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCode() {
                    return code;
                }

                public void setCode(int code) {
                    this.code = code;
                }

                public List<CitiesBean> getCities() {
                    return cities;
                }

                public void setCities(List<CitiesBean> cities) {
                    this.cities = cities;
                }

                public static class CitiesBean {
                    /**
                     * name : ARICA
                     * code : 15101
                     * districts : [{"name":"CODPA","code":"ACZ"},{"name":"GUANACAHUA","code":"AGN"},{"name":"MOLINOS","code":"AMO"},{"name":"ARICA","code":"ARI"},{"name":"ESQUINA","code":"AQE"},{"name":"AUSIPAR","code":"APR"},{"name":"ALTO RAMIREZ","code":"ARZ"},{"name":"TIMAR","code":"ATI"},{"name":"SOBRAYA","code":"AYA"},{"name":"SORA","code":"SOR"},{"name":"RANCHO ARICA","code":"RAR"},{"name":"POCONCHILE","code":"POC"},{"name":"CUZ CUZ - ARICA","code":"CZZ"},{"name":"AZAPA","code":"AZA"},{"name":"VILLA FRONTERA","code":"AVF"}]
                     */

                    private String name;
                    private int code;
                    private List<DistrictsBean> districts;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getCode() {
                        return code;
                    }

                    public void setCode(int code) {
                        this.code = code;
                    }

                    public List<DistrictsBean> getDistricts() {
                        return districts;
                    }

                    public void setDistricts(List<DistrictsBean> districts) {
                        this.districts = districts;
                    }

                    public static class DistrictsBean {
                        /**
                         * name : CODPA
                         * code : ACZ
                         */

                        private String name;
                        private String code;

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }
                    }
                }
            }
        }
    }
}

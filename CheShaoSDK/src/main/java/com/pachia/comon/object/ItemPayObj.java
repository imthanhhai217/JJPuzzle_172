package com.pachia.comon.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemPayObj {
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    @SerializedName("config")
    @Expose
    private Config config;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Config getConfig() {
        if (config == null) {
            return getDefautConfig();
        }
        return config;
    }

    public Config getDefautConfig() {
        Config cf = new Config();
        cf.setColumns(2);
        cf.setRows(2);
        cf.setScroll(1);
        return cf;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static class Item{
        @SerializedName("item_no")
        @Expose
        private String itemNo;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("xt")
        @Expose
        private String xt;
        @SerializedName("price")
        @Expose
        private Double price;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("unit")
        @Expose
        private String unit;
        @SerializedName("item_value")
        @Expose
        private Integer itemValue;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("config")
        @Expose
        private ConfigItem configItem;


        public String getItemNo() {
            return itemNo;
        }

        public void setItemNo(String itemNo) {
            this.itemNo = itemNo;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getXt() {
            return xt;
        }

        public void setXt(String xt) {
            this.xt = xt;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Integer getItemValue() {
            return itemValue;
        }

        public void setItemValue(Integer itemValue) {
            this.itemValue = itemValue;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public ConfigItem getConfigItem() {
            return configItem;
        }

        public void setConfigItem(ConfigItem configItem) {
            this.configItem = configItem;
        }

        public static class ConfigItem{
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("url")
            @Expose
            private String url;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class Config{
        @SerializedName("rows")
        @Expose
        private Integer rows;
        @SerializedName("columns")
        @Expose
        private Integer columns;
        @SerializedName("scroll")
        @Expose
        private Integer scroll;

        public Integer getRows() {
            return rows;
        }

        public void setRows(Integer rows) {
            this.rows = rows;
        }

        public Integer getColumns() {
            return columns;
        }

        public void setColumns(Integer columns) {
            this.columns = columns;
        }

        public Integer getScroll() {
            return scroll;
        }

        public void setScroll(Integer scroll) {
            this.scroll = scroll;
        }
    }

}

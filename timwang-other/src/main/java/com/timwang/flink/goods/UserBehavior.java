package com.timwang.flink.goods;

import java.io.Serializable;

/**
 * @author wangjun
 * @date 2019/3/26
 */
public class UserBehavior implements Serializable {
    /**
     * 用户 ID
     */
    public long userId;
    /**
     * 商品 ID
     */
    public long itemId;
    /**
     * 商品类目 ID
     */
    public int categoryId;
    /**
     * 用户行为, 包括("pv", "buy", "cart", "fav")
     */
    public String behavior;
    /**
     * 行为发生的时间戳，单位秒
     */
    public long timestamp;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

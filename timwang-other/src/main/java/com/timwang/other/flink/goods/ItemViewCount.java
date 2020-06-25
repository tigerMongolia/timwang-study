package com.timwang.other.flink.goods;

/**
 * @author wangjun
 * @date 2019/3/26
 */
public class ItemViewCount {
    public long itemId; // 商品 ID
    public long windowEnd; // 窗口结束时间戳
    public long viewCount; // 商品的点击量
    public static ItemViewCount of(long itemId, long windowEnd, long viewCount) {
        ItemViewCount result = new ItemViewCount();
        result.itemId = itemId;
        result.windowEnd = windowEnd;
        result.viewCount = viewCount;
        return result;
    }
}
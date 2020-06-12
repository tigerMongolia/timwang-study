package com.timwang.warmup.core;

/**
 * @author wangjun
 * @date 2019-12-10
 */
public class APractice2 {
    /**
     * 给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
     * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
     * 说明：不允许修改给定的链表。
     *
     * 示例 1：
     *
     * 输入：head = [3,2,0,-4], pos = 1
     * 输出：tail connects to node index 1
     * 解释：链表中有一个环，其尾部连接到第二个节点。
     *
     *
     * 示例 2：
     *
     * 输入：head = [1,2], pos = 0
     * 输出：tail connects to node index 0
     * 解释：链表中有一个环，其尾部连接到第一个节点。
     *
     *
     * 示例 3：
     *
     * 输入：head = [1], pos = -1
     * 输出：no cycle
     * 解释：链表中没有环。
     *
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/linked-list-cycle-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * Definition for singly-linked list.
     * class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) {
     *         val = x;
     *         next = null;
     *     }
     * }
     *
     *
     *
     */
    public class Solution {
        /**
         * 双指针法，类似小时候做的追及问题，想象一下圆形跑道，两个人跑步，如果一个人跑的快，一个人跑的慢，
         * 那么不管两个人从哪个位置出发，跑的过程中两人一定会相遇。
         * 这类链表题目一般都是使用双指针法解决的，例如寻找距离尾部第K个节点、寻找环入口、寻找公共尾部入口等。
         * @param head ListNode
         * @return ListNode
         */
        public ListNode detectCycle(ListNode head) {
            return null;
        }
    }
}

package cn.com.ubankers.www.utils;

public interface NetEventHandle {
        /**
         * 网络状态码
         * @param netCode
         */
        void netState(NetReceiver.NetState netCode);
    }

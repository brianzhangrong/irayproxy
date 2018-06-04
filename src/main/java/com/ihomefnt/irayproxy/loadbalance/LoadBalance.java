package com.ihomefnt.irayproxy.loadbalance;

import java.util.List;

@FunctionalInterface
public interface LoadBalance {
	String select(List<String> selectList);
}

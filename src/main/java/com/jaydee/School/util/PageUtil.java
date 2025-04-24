//package com.jaydee.School.util;
//
//import java.awt.print.Pageable;
//
//import org.springframework.data.domain.PageRequest;
//
//public interface PageUtil {
//	int DEFAULT_PAGE_LIMIT = 10;
//	int DEFAULT_PAGE_NUMBER = 1;
//	
//	static Pageable getPageable(int pagenNumber, int pageSize) {
//		if(pagenNumber < DEFAULT_PAGE_NUMBER) {
//			pagenNumber = DEFAULT_PAGE_NUMBER;
//		}
//		
//		if(pageSize < 1) {
//			pageSize = DEFAULT_PAGE_LIMIT;
//		}
//		Pageable pageable = PageRequest.of(pagenNumber, pageSize);
//		return pageable;
//	}
//	
//}

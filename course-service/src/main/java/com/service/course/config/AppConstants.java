package com.service.course.config;

import java.io.File;

public class AppConstants {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_PAGE_SORT_BY = "title";

    public static final String DEFAULT_PAGE_SORT_DIR = "asc";
    public static final String COURSE_BANNER_UPLOAD_DIR = "uploads"+ File.separator + "courses" + File.separator + "banner"+File.separator;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GUEST = "ROLE_GUEST";

    public static final String DEFAULT_PROFILE_PATH = "uploads/courses/banner";

    public static final String DEFAULT_CATEGORY_URL = "http://category-service/api/v1";
    public static final String DEFAULT_VIDEO_BASE_URL = "http://video-service/api/v1";
}

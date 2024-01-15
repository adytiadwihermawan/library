package com.miniproject.library;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
class LibraryApplicationTest {

    @Test
     void applicationContextLoaded() {
    }

    @Test
     void applicationContextTest() {
        LibraryApplication.main(new String[] {});
    }
}
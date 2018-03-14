#include "../Reorientation/Reorientation.h"
#include "gtest/gtest.h"

namespace {
	TEST(ReorientationTest, doesFooReturn10){
		EXPECT_EQ(2, foo(1));
	}
	TEST(ReorientationTest, doesFooReturn10Success){
	    EXPECT_EQ(10, foo(100));
	}
}

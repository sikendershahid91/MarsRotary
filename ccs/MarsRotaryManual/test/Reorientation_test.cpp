#include "../Reorientation/Reorientation.h"
#include "gtest/gtest.h"

namespace {
	TEST(ReorientationTest, doesFooReturn10){
		EXPECT_EQ(2, foo(1));
	}
	TEST(ReorientationTest, doesFooReturn10Success){
	    EXPECT_EQ(10, foo(100));
	}
	TEST(ReorientationTest, TestingMacro){
		EXPECT_EQ(0x0808, COMBINE_PACKET(0x08, 0x08)); 
	}
}

#include "ShortTest.h"
#include <assert.h>
#include "Set.h"
#include "SetIterator.h"
#include <iostream>
#include <exception>

void testAll() { 
	Set s;
	assert(s.isEmpty() == true);
	assert(s.size() == 0); 
	assert(s.add(5)==true);
	assert(s.add(1)==true);
	assert(s.add(10)==true);
	assert(s.add(7)==true);
	assert(s.add(1)==false);
	assert(s.add(10)==false);
	assert(s.add(-3)==true);
	assert(s.size() == 5);
	assert(s.search(10) == true);
	assert(s.search(16) == false);
	assert(s.remove(1) == true);
	assert(s.remove(6) == false);
	assert(s.size() == 4);


	SetIterator it = s.iterator();
	it.first();
	int sum = 0;
	while (it.valid()) {
		TElem e = it.getCurrent();
		sum += e;
		it.next();
	}
	assert(sum == 19);

//////////////////////////////////////

	Set s2;
	s2.add(1);
	s2.add(2);
	s2.add(3);
	s2.add(4);
	s2.add(5);
	SetIterator it2 = s2.iterator();
	it2.first();
	TElem e1 = it2.getCurrent();

	it2.next();
	it2.next();
	it2.next();
	it2.jumpBackward(3);
	TElem e2 = it2.getCurrent();

	assert(e1 == e2);

	try {
		it2.jumpBackward(3);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}

	try {
		it2.jumpBackward(0);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}

	try {
		it2.jumpBackward(7);
		assert(false);
	}
	catch (std::exception& e) {
		assert(true);
	}


}


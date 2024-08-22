#include "ShortTest.h"
#include "MultiMap.h"
#include "MultiMapIterator.h"
#include <assert.h>
#include <vector>
#include<iostream>

void testAll() {
	MultiMap m;
	m.add(1, 100);
	m.add(2, 200);
	m.add(3, 300);
	m.add(1, 500);
	m.add(2, 600);
	m.add(4, 800);

	assert(m.size() == 6);

	assert(m.remove(5, 600) == false);
	assert(m.remove(1, 500) == true);

	assert(m.size() == 5);

    vector<TValue> v;
	v=m.search(6);
	assert(v.size()==0);

	v=m.search(1);
	assert(v.size()==1);

	assert(m.isEmpty() == false);

	MultiMapIterator im = m.iterator();
	assert(im.valid() == true);
	while (im.valid()) {
		im.getCurrent();
		im.next();
	}
	assert(im.valid() == false);
	im.first();
	assert(im.valid() == true);


//////////////////////////////

	MultiMap m1;
	m1.add(1, 100);
	m1.add(1, 500);
	m1.add(2, 200);
	m1.add(3, 300);
	m1.add(2, 600);
	m1.add(4, 800);
	
	MultiMapIterator im1 = m1.iterator();
	im1.first();
	auto elem = im1.getCurrent();
	im1.next();
	im1.next();
	im1.jumpBackward(2);
	assert(im1.getCurrent().first == elem.first);
	assert(im1.getCurrent().second == elem.second);
	
	im1.next();
	im1.next();
	auto elem1 = im1.getCurrent();
	im1.next();
	im1.jumpBackward(1);
	assert(im1.getCurrent().first == elem1.first);
	assert(im1.getCurrent().second == elem1.second);

	try
	{
		im1.jumpBackward(10);
		assert(false);
	}
	catch (std::exception&)
	{
		assert(true);
	 }
	
	
}

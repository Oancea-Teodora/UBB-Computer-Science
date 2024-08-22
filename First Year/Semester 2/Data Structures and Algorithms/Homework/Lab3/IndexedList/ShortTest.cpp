#include <assert.h>
#include <exception>
#include <iostream>
#include "ShortTest.h"
#include "IndexedList.h"
#include "ListIterator.h"

using namespace std;


void testAll() {
    IndexedList list = IndexedList();
    assert(list.isEmpty());
    list.addToEnd(1);
    assert(list.size() == 1);
    list.addToPosition(0,2);
    assert(list.size() == 2);

    ListIterator it = list.iterator();
    assert(it.valid());
    it.next();
    assert(it.getCurrent() == 1);
    it.first();
    assert(it.getCurrent() == 2);

    assert(list.search(1) == 1);
    assert(list.setElement(1,3) == 1);
    assert(list.getElement(1) == 3);
    assert(list.remove(0) == 2);
    assert(list.size() == 1);

 ////////////////////////////////////////////

    IndexedList list1 = IndexedList();
    list1.addToEnd(4);
    list1.addToEnd(5);   
    list1.addToEnd(6);
    list1.addToEnd(7);

    ListIterator it1 = list1.iterator();
    it1.first();
    assert(it1.remove() == 4);
    assert(it1.getCurrent() == 5);
    it1.next();
    
    assert(it1.getCurrent() == 6);
    assert(it1.remove() == 6);
    
    assert(it1.getCurrent() == 7);
    assert(it1.remove() == 7);
    assert(it1.valid() == false);

}

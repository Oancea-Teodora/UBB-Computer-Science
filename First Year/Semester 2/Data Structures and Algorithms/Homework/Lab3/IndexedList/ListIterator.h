#pragma once
#include "IndexedList.h"


class ListIterator{
    //DO NOT CHANGE THIS PART
	friend class IndexedList;
private:
	IndexedList& list;
	//TODO - Representation
    int current;

		
    ListIterator(IndexedList& lista);
public:
    void first();
    void next();
    bool valid() const;
    TElem getCurrent() const;
    TElem remove();

};


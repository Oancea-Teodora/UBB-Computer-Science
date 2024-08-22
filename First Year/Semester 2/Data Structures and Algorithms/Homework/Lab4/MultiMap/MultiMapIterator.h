#pragma once
#include "MultiMap.h"

class MultiMap;

class MultiMapIterator
{
	friend class MultiMap;

private:
	MultiMap& col;
	//TODO - Representation
	int currentPos;
	Node* currentNode;
	Node* prevNode;

	int nrnext=0;

	//DO NOT CHANGE THIS PART
	MultiMapIterator(MultiMap& c);

public:
	TElem getCurrent()const;
	bool valid() const;
	void next();
	void first();
	void jumpBackward(int k);
};


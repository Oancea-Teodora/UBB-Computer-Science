#include <assert.h>
#include "Matrix.h"
#include "MatrixIterator.h"
#include <iostream>
using namespace std;

void testAll() { 
	Matrix m(4, 4);
	assert(m.nrLines() == 4);
	assert(m.nrColumns() == 4);	
	m.modify(1, 1, 5);
	assert(m.element(1, 1) == 5);
	TElem old = m.modify(1, 1, 6);
	assert(m.element(1, 2) == NULL_TELEM);
	assert(old == 5);

	MatrixIterator it2 = m.iterator();
	int nonZeroCount = 0;
	while (it2.valid()) {
		TElem elem = it2.getCurrent();
		assert(elem != NULL_TELEM); // Ensure the element is indeed non-zero.
		nonZeroCount++;
		it2.next(); // Move to the next element.
	}

	assert(nonZeroCount == 1);
}
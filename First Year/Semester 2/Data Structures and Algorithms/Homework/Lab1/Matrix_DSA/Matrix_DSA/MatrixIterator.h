#pragma once
#include "Matrix.h"

class MatrixIterator {
private:
	const Matrix& matrix;
	int currentRow;
	int currentCol;

public:
	MatrixIterator(const Matrix& matrix);

	// Moves the iterator to the next valid element
	void next();

	// Checks if the iterator reached the end
	bool valid() const;

	// Returns the element at the current position
	TElem getCurrent() const;
};
#include "MatrixIterator.h"

MatrixIterator::MatrixIterator(const Matrix& matrix) : matrix(matrix), currentRow(0), currentCol(0) {
	// Initialize iterator to the first valid element
	while (currentRow < matrix.nrLines() && matrix.element(currentRow, currentCol) == NULL_TELEM) {
		currentCol++;
		if (currentCol == matrix.nrColumns()) {
			currentRow++;
			currentCol = 0;
		}
	}
}

void MatrixIterator::next() {
	// Move to the next position
	do {
		currentCol++;
		if (currentCol == matrix.nrColumns()) {
			currentRow++;
			currentCol = 0;
		}
	} while (currentRow < matrix.nrLines() && matrix.element(currentRow, currentCol) == NULL_TELEM);
}

bool MatrixIterator::valid() const {
	// Check if iterator is still within bounds
	return currentRow < matrix.nrLines();
}

TElem MatrixIterator::getCurrent() const {
	// Return the element at the current position
	return matrix.element(currentRow, currentCol);
}
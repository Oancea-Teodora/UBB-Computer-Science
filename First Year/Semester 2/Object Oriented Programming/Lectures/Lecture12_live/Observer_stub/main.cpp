#include "Observer_stub.h"
#include <QtWidgets/QApplication>
#include "ShoppingBasketWidget.h"

int main(int argc, char *argv[])
{
	QApplication a(argc, argv);
	
	ShoppingBasket basket{};
	basket.addProduct(Product{ "Apples", 18 });
	basket.addProduct(Product{ "Butter", 16 });
	basket.addProduct(Product{ "Orange juice", 22 });
	basket.addProduct(Product{ "Surgical mask", 3 });

	ShoppingBasketWidget window1{basket};
	basket.addObserver(&window1);
	window1.show();

	ShoppingBasketWidget window2{basket};
	basket.addObserver(&window2);
	window2.show();

	return a.exec();
}

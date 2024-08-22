#include "ModelView_stub.h"
#include <QtWidgets/QApplication>
#include "ShoppingBasket.h"
#include "ShoppingBasketWindow.h"
#include <qsortfilterproxymodel.h>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    ShoppingBasket basket{};
    basket.addProduct(Product{ "Apples", 18, 6 });
    basket.addProduct(Product{ "Butter", 16, 1 });
    basket.addProduct(Product{ "Orange juice", 22, 1 });
    basket.addProduct(Product{ "Surgical mask", 3, 10 });

    ProductsModel* productsModel = new ProductsModel{basket};

    ShoppingBasketWindow window1{productsModel};
    window1.show();

    ShoppingBasketWindow window2{ productsModel };
    window2.show();

    QSortFilterProxyModel* proxyModel = new QSortFilterProxyModel{};
    proxyModel->setSourceModel(productsModel);
    proxyModel->setFilterRegularExpression(QRegularExpression(".*j.*", QRegularExpression::CaseInsensitiveOption));
    proxyModel->setFilterKeyColumn(0);
    ShoppingBasketWindow window3{ proxyModel };
    window3.show();

    return a.exec();
}

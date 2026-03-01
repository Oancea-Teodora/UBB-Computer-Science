
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SCHITA
{
    public partial class Form1: Form
    {
        SqlConnection cs = new SqlConnection("Data Source=TEO; Initial Catalog = Test; Integrated Security = True");   // BAZA DE DATE  
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();

        SqlDataAdapter daChild;
        DataSet dsChild;

        public Form1()
        {
            InitializeComponent();
            da.SelectCommand = new SqlCommand("SELECT * FROM CoffeeShops", cs);     // AICI PUI TABELA PARINTE
            ds.Clear();
            da.Fill(ds);
            dgvParent.DataSource = ds.Tables[0];
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                SqlCommandBuilder builder = new SqlCommandBuilder(daChild);
                daChild.Update(dsChild.Tables[0]);

                MessageBox.Show("Child‐table changes saved successfully.","Success",MessageBoxButtons.OK,MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error saving child changes: " + ex.Message);
            }
        }

        private void dgvParent_SelectionChanged(object sender, EventArgs e)
        {
            try
            {
                if (dgvParent.CurrentRow != null)
                {
                    int ID = (int)dgvParent.CurrentRow.Cells["ShopID"].Value;                  // AICI E PRIMARY KEY UL DE LA PARINTE
                    SqlCommand cmd2 = new SqlCommand("SELECT * FROM Employers WHERE ShopID = @ID", cs);    // AICI PUI TABELA COPILULUI SI FK (COPIL) = PK (PARINTE)
                    cmd2.Parameters.AddWithValue("@ID", ID);

                    daChild = new SqlDataAdapter(cmd2);
                    dsChild = new DataSet();
                    daChild.Fill(dsChild);
                    dgvChild.DataSource = dsChild.Tables[0];
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }
    }
}

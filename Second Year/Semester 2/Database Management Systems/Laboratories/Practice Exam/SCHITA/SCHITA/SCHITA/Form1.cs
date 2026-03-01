
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
    public partial class Form1 : Form
    {
        SqlConnection cs = new SqlConnection("Data Source=TEO; Initial Catalog = TestPractic; Integrated Security = True");   
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();

        SqlDataAdapter daChild;
        DataSet dsChild;

        public Form1()
        {
            InitializeComponent();
            da.SelectCommand = new SqlCommand("SELECT * FROM Cities", cs);    
            ds.Clear();
            da.Fill(ds);
            dgvCities.DataSource = ds.Tables[0];
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                SqlCommandBuilder builder = new SqlCommandBuilder(daChild);
                daChild.Update(dsChild.Tables[0]);

                MessageBox.Show("Child‐table changes saved successfully.", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error saving child changes: " + ex.Message);
            }
        }

        private void dgvCities_SelectionChanged(object sender, EventArgs e)
        {
            try
            {
                if (dgvCities.CurrentRow != null)
                {
                    int ID = (int)dgvCities.CurrentRow.Cells["CityID"].Value;                  
                    SqlCommand cmd2 = new SqlCommand("SELECT * FROM FavouriteBakeries WHERE CityID = @ID", cs);   
                    cmd2.Parameters.AddWithValue("@ID", ID);

                    daChild = new SqlDataAdapter(cmd2);
                    dsChild = new DataSet();
                    daChild.Fill(dsChild);
                    dgvFavouriteBakeries.DataSource = dsChild.Tables[0];
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}

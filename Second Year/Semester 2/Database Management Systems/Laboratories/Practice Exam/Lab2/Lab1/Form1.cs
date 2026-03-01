using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Configuration;

namespace Lab1
{
    public partial class Form1: Form
    {
        public static string server = ConfigurationManager.AppSettings.Get("server");
        public static string database = ConfigurationManager.AppSettings.Get("database");
        public static string parentTable = ConfigurationManager.AppSettings.Get("parentTable");
        public static string childTable = ConfigurationManager.AppSettings.Get("childTable");
        public static string parentPrimaryKey = ConfigurationManager.AppSettings.Get("parentPrimaryKey");
        public static string childForeignKey = ConfigurationManager.AppSettings.Get("childForeignKey");
        public static string columnName = ConfigurationManager.AppSettings.Get("column_name");

        //  SqlConnection cs = new SqlConnection("Data Source=TEO; Initial Catalog = Book_Club; Integrated Security = True"); 
        SqlConnection sqlConnection = new SqlConnection("Data Source=" + server + ";Database=" + database + ";Integrated Security=SSPI");
       // SqlDataAdapter da = new SqlDataAdapter();
        DataSet dataSet = new DataSet();

        SqlDataAdapter parentDataAdapter = new SqlDataAdapter();
        SqlDataAdapter childDataAdapter = new SqlDataAdapter();

        BindingSource parentBindingSource = new BindingSource();
        BindingSource childBindingSource = new BindingSource();

        SqlCommandBuilder parentBuilder = new SqlCommandBuilder();
        SqlCommandBuilder childBuilder = new SqlCommandBuilder();

        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            label1.Text = "Parent Table: " + parentTable;
            label2.Text = "Child Table: " + childTable;

            parentDataAdapter = new SqlDataAdapter("SELECT * FROM " + parentTable, sqlConnection);
            childDataAdapter = new SqlDataAdapter("SELECT * FROM " + childTable, sqlConnection);

            SqlCommandBuilder parentBuilder = new SqlCommandBuilder(parentDataAdapter);
            SqlCommandBuilder childBuilder = new SqlCommandBuilder(childDataAdapter);
          
            parentDataAdapter.Fill(dataSet, parentTable);
            childDataAdapter.Fill(dataSet, childTable);

            DataColumn parentPK = dataSet.Tables[parentTable].Columns[parentPrimaryKey];
            DataColumn childFK = dataSet.Tables[childTable].Columns[childForeignKey];

            DataRelation relation = new DataRelation("fk_parent_child", parentPK, childFK);
            dataSet.Relations.Add(relation);

            parentBindingSource.DataSource = dataSet;
            parentBindingSource.DataMember = parentTable;

            childBindingSource.DataSource = parentBindingSource;
            childBindingSource.DataMember = "fk_parent_child";

            dataGridView1.DataSource = parentBindingSource;
            dataGridView2.DataSource = childBindingSource;

        }

        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                foreach (DataRow row in dataSet.Tables[childTable].Rows)
                {
                    if (row.RowState == DataRowState.Deleted) continue;

                    if (string.IsNullOrWhiteSpace(row[columnName].ToString()))
                    {
                        MessageBox.Show($"Error: {columnName} cannot be empty in the child table.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        return;
                    }
                    if(columnName == "rating")
                    {
                        if (!int.TryParse(row[columnName]?.ToString(), out int _))
                        {
                            MessageBox.Show("Error: 'rating' must be a valid number.", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                            return;
                        }
                    }
                }

                parentDataAdapter.Update(dataSet, parentTable);
                childDataAdapter.Update(dataSet, childTable);
                MessageBox.Show("Updated with succes!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                sqlConnection.Close();
            }

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void Form1_Load_1(object sender, EventArgs e)
        {

        }

    }
}

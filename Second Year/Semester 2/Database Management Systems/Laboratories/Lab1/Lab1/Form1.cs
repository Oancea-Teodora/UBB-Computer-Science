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

namespace Lab1
{
    public partial class Form1: Form
    {
        SqlConnection cs = new SqlConnection("Data Source=TEO; Initial Catalog = Book_Club; Integrated Security = True"); 
        SqlDataAdapter da = new SqlDataAdapter();
        DataSet ds = new DataSet();

        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            da.SelectCommand = new SqlCommand("SELECT * FROM Members", cs);
            ds.Clear();
            da.Fill(ds);
            dataGridView1.DataSource = ds.Tables[0];

        }

        //afisare
        private void dataGridView1_SelectionChanged(object sender, EventArgs e)
        {
            try
            {
                if (dataGridView1.CurrentRow != null)
                {
                    int memberID = (int)dataGridView1.CurrentRow.Cells["members_ID"].Value;
                    SqlCommand cmd2 = new SqlCommand("SELECT * FROM Book_Requests WHERE fk_BookRequests_Members = @memberID", cs);
                    cmd2.Parameters.AddWithValue("@memberID", memberID);

                    SqlDataAdapter daChild = new SqlDataAdapter(cmd2);
                    DataSet dsChild = new DataSet();
                    daChild.Fill(dsChild);
                    dataGridView2.DataSource = dsChild.Tables[0];
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }

        }

        //adaugare
        private void button2_Click(object sender, EventArgs e)
        {
            try
            {
                if (dataGridView1.CurrentRow != null)
                {                    
                    int memberID = (int)dataGridView1.CurrentRow.Cells["members_ID"].Value;
                    string title = textBox1.Text;
                    string author = textBox2.Text;
                    string status = textBox3.Text;
                    string requestID2 = textBox4.Text;
                    int requestID = int.Parse(requestID2);

                    if (string.IsNullOrEmpty(title))
                    {
                        MessageBox.Show("Error: Title cannot be empty!", "Validation Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                        return;
                    }

                    SqlCommand insertCommand = new SqlCommand(
                        "INSERT INTO Book_Requests (request_id, book_title, book_author, request_status, fk_BookRequests_Members) " +
                        "VALUES (@requestID, @title, @author, @status, @member)",
                        cs
                    );

                    insertCommand.Parameters.AddWithValue("@requestID", requestID);
                    insertCommand.Parameters.AddWithValue("@title", title);
                    insertCommand.Parameters.AddWithValue("@author", author);
                    insertCommand.Parameters.AddWithValue("@status", status);
                    insertCommand.Parameters.AddWithValue("@member", memberID);

                    cs.Open();
                    insertCommand.ExecuteNonQuery();
                    cs.Close();
                    dataGridView1_SelectionChanged(null, null);

                    MessageBox.Show("Book request added!");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }
        }

        //stergere
        private void button4_Click(object sender, EventArgs e)
        {
            try
            {
                int requestID = (int)dataGridView2.CurrentRow.Cells["request_id"].Value;
                da.DeleteCommand = new SqlCommand("DELETE FROM Book_Requests WHERE request_id = @requestID", cs);
                da.DeleteCommand.Parameters.AddWithValue("@requestID", requestID);

                cs.Open();
                da.DeleteCommand.ExecuteNonQuery();
                cs.Close();
                dataGridView1_SelectionChanged(null, null);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                cs.Close();
            }

        }

        //modificare 
        private void button3_Click(object sender, EventArgs e)
        {
            try
            {
                if (dataGridView1.CurrentRow != null)
                {
                   // int requestID = (int)dataGridView2.CurrentRow.Cells["request_id"].Value;
                    string requestID2 = textBox4.Text;
                    int requestID = int.Parse(requestID2);
                    string title = textBox1.Text;
                    string author = textBox2.Text;
                    string status = textBox3.Text;

                    SqlCommand updateCommand = new SqlCommand(
                        "UPDATE Book_Requests SET book_title=@title, book_author=@author, request_status=@status WHERE request_id=@requestID", cs );

                    updateCommand.Parameters.AddWithValue("@requestID", requestID);
                    updateCommand.Parameters.AddWithValue("@title", title);
                    updateCommand.Parameters.AddWithValue("@author", author);
                    updateCommand.Parameters.AddWithValue("@status", status);

                    cs.Open();
                    updateCommand.ExecuteNonQuery();
                    cs.Close();
                    dataGridView1_SelectionChanged(null, null);

                    MessageBox.Show("Book request updated!");
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

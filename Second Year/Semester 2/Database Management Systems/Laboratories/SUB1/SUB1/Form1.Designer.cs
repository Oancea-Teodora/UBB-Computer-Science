namespace SUB1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.dgvDentalCabinet = new System.Windows.Forms.DataGridView();
            this.dgvDentists = new System.Windows.Forms.DataGridView();
            this.btnSave_Click = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dgvDentalCabinet)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvDentists)).BeginInit();
            this.SuspendLayout();
            // 
            // dgvDentalCabinet
            // 
            this.dgvDentalCabinet.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvDentalCabinet.Location = new System.Drawing.Point(12, 38);
            this.dgvDentalCabinet.Name = "dgvDentalCabinet";
            this.dgvDentalCabinet.RowHeadersWidth = 51;
            this.dgvDentalCabinet.RowTemplate.Height = 24;
            this.dgvDentalCabinet.Size = new System.Drawing.Size(372, 235);
            this.dgvDentalCabinet.TabIndex = 0;
            this.dgvDentalCabinet.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridView1_CellContentClick);
            // 
            // dgvDentists
            // 
            this.dgvDentists.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvDentists.Location = new System.Drawing.Point(426, 38);
            this.dgvDentists.Name = "dgvDentists";
            this.dgvDentists.RowHeadersWidth = 51;
            this.dgvDentists.RowTemplate.Height = 24;
            this.dgvDentists.Size = new System.Drawing.Size(362, 235);
            this.dgvDentists.TabIndex = 1;
            // 
            // btnSave_Click
            // 
            this.btnSave_Click.Location = new System.Drawing.Point(347, 319);
            this.btnSave_Click.Name = "btnSave_Click";
            this.btnSave_Click.Size = new System.Drawing.Size(112, 29);
            this.btnSave_Click.TabIndex = 2;
            this.btnSave_Click.Text = "button1";
            this.btnSave_Click.UseVisualStyleBackColor = true;
            this.btnSave_Click.Click += new System.EventHandler(this.btnSave_Click_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.btnSave_Click);
            this.Controls.Add(this.dgvDentists);
            this.Controls.Add(this.dgvDentalCabinet);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.dgvDentalCabinet)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.dgvDentists)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.DataGridView dgvDentalCabinet;
        private System.Windows.Forms.DataGridView dgvDentists;
        private System.Windows.Forms.Button btnSave_Click;
    }
}


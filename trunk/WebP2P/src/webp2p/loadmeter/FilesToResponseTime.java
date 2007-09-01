package webp2p.loadmeter;

/**
 * Tupla para representar os arquivos e seus respectivos tempos de respostas.
 * @author Jo�o Arthur Brunet Monteiro - jarthur@dsc.ufcg.edu.br
 */
public class FilesToResponseTime {
		
		private String file;
		private double responseTime;
		
		public FilesToResponseTime(String file, double responseTime2) {
			this.file = file;
			this.responseTime = responseTime2;
		}
		
		public String getFile() {
			return file;
		}
		
		public double getResponseTime() {
			return responseTime;
		}

		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((file == null) ? 0 : file.hashCode());
			long temp;
			temp = Double.doubleToLongBits(responseTime);
			result = PRIME * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final FilesToResponseTime other = (FilesToResponseTime) obj;
			if (file == null) {
				if (other.file != null)
					return false;
			} else if (!file.equals(other.file))
				return false;
			if (Double.doubleToLongBits(responseTime) != Double.doubleToLongBits(other.responseTime))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return this.file + " " + this.responseTime;
		}
}
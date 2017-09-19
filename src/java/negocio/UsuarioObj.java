
package negocio;
public class UsuarioObj {
    private String codUsuario, nomUsuario; 
    private String apeUsuario, nickUsuario; 
    private String pasUsuario, dirUsuario;
    private String fechaNac,tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getApeUsuario() {
        return apeUsuario;
    }

    public void setApeUsuario(String apeUsuario) {
        this.apeUsuario = apeUsuario;
    }

    public String getNickUsuario() {
        return nickUsuario;
    }

    public void setNickUsuario(String nickUsuario) {
        this.nickUsuario = nickUsuario;
    }

    public String getPasUsuario() {
        return pasUsuario;
    }

    public void setPasUsuario(String pasUsuario) {
        this.pasUsuario = pasUsuario;
    }

    public String getDirUsuario() {
        return dirUsuario;
    }

    public void setDirUsuario(String dirUsuario) {
        this.dirUsuario = dirUsuario;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

}

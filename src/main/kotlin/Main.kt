import java.io.File
import java.io.FileReader
import java.io.FileWriter

class Functionary (val id: Int, val nome: String, val cargo: String, val salario: Float) {
    fun getData(): Map<String, Any> {
        return mapOf<String, Any>(
            "id" to this.id,
            "nome" to this.nome,
            "cargo" to this.cargo,
            "salario" to this.salario
        )
    }

    companion object {

        fun registerFunctionary (file: File) {
            val reader = FileReader(file)
            val lines = reader.readLines().toMutableList()
            reader.close()

            var nome = ""
            var cargo = ""
            var salario = ""
            while (true) {
                print("Insira o nome do funcionário: ")
                nome = readln()

                if (nome != "") {
                    break
                } else {
                    print(red() + "Por favor insira um nome válido.\n\n" + reset())
                    continue
                }
            }
            while (true) {
                print("Insira o cargo do funcionário: ")
                cargo = readln()

                if (cargo != "") {
                    break
                } else {
                    print(red() + "Por favor insira um cargo válido.\n\n" + reset())
                    continue
                }
            }
            while (true) {
                print("Insira o salário do funcionário: ")
                salario = readln()

                if (salario.matches("[0-9]+(\\.[0-9]+)?".toRegex())) {
                    break
                } else {
                    print(red() + "Por favor insira um salário válido.\n\n" + reset())
                    continue
                }
            }

            val novoFuncionario = Functionary(lines.size+1, nome, cargo, salario.toFloat())
            val dataNovoFuncionario = novoFuncionario.getData()

            val novaLinha = "${dataNovoFuncionario["id"]},${dataNovoFuncionario["nome"]},${dataNovoFuncionario["cargo"]},${dataNovoFuncionario["salario"]}"
            lines.add(novaLinha)

            val writer = FileWriter(file)
            writer.write(lines.joinToString("\n"))
            writer.close()

            print(green() + "Funcionário cadastrado com sucesso!\n\n" + reset())
        }

        fun listFunctionary(file: File) {
            val reader = FileReader(file)
            val lines = reader.readLines()
            reader.close()

            if (lines.isEmpty()) {
                print(red() + "Não há nenhum funcionário cadastrado para exibir.\n\n" + reset())
                return
            }
            table(lines)
        }

        fun deleteFunctionary(file: File) {
            val reader = FileReader(file)

            val lines = reader.readLines().toMutableList()
            reader.close()

            print("Digite o nome do funcionário que deseja deletar (Insira \"0\" para cancelar a operação): ")
            val nome = readln()

            if (nome == "0") {
                print(red() + "Operação cancelada\n\n" + reset())
                return
            }

            val index = mutableListOf(-1)

            val searchFunctionaries = File("searchFunctionaries.csv")
            val readerSearchFunctionaries = FileReader(searchFunctionaries)
            val linesSearchFunctionaries = readerSearchFunctionaries.readLines().toMutableList()

            for ((i, line) in lines.withIndex()) {
                val fields = line.split(",")
                if (fields[1] == nome) {
                    if (index[0] == -1) {
                        index.removeAt(0)
                    }
                    index.add(i)

                    val novaLinha = "${fields[0]},${fields[1]},${fields[2]},${fields[3]}"
                    linesSearchFunctionaries.add(novaLinha)

                    val writerSearchFunctionaries = FileWriter(searchFunctionaries)
                    writerSearchFunctionaries.write(linesSearchFunctionaries.joinToString("\n"))
                }
            }

            if (index[0] == -1) {
                print(red() + "Nenhum funcionário com o nome informado foi encontrado.\n\n" + reset())
                return
            }

            if (linesSearchFunctionaries.size > 1) {
                print(yellow() + "Foram encontrados mais de um funcionário com o nome informado. Informe o id do funcionário a ser deletado\n\n" + reset())
                table(linesSearchFunctionaries)

                while (true) {
                    print("Id do funcionário a ser deletado: (Insira \"0\" para cancelar a operação): ")
                    val respostaDeleta = readln()

                    when (respostaDeleta) {
                        "0" -> {
                            print(red() + "Operação cancelada\n\n" + reset())
                            return
                        }
                        "" -> {
                            print(red() + "Por favor insira um valor válido.\n\n" + reset())
                            continue
                        }
                        else -> {
                            if (respostaDeleta.toInt()-1 in lines.indices) {
                                lines.removeAt(respostaDeleta.toInt()-1)

                                val writer = FileWriter(file)
                                writer.write(lines.joinToString("\n"))
                                writer.close()
                                val writerSearchFunctionaries = FileWriter(searchFunctionaries)
                                writerSearchFunctionaries.write("")

                                print(green() + "Funcionário deletado com sucesso\n\n" + reset())
                                return
                            } else {
                                print(red() + "Id não encontrado.\n\n" + reset())
                                continue
                            }
                        }
                    }
                }
            }

            lines.removeAt(index[0])

            val writer = FileWriter(file)
            writer.write(lines.joinToString("\n"))
            writer.close()

            print(green() + "Funcionário deletado com sucesso\n\n" + reset())
        }
    }
}

fun main() {
    val file = File("funcionarios.csv")

    while (true) {

        println("Insira o número da funcionalidade que deseja utilizar: ")
        print("1 - Cadastrar funcionário\n2 - Listar os funcionários\n3 - Deletar funcionário\n4 - Encerrar o programa\n\nResposta: ")

        when (readln()) {
            "1" -> {
                Functionary.registerFunctionary(file)
            }
            "2" -> {
                Functionary.listFunctionary(file)
            }
            "3" -> {
                Functionary.deleteFunctionary(file)
            }
            "4" -> {
                println("Programa encerrado")
                break
            }
            else -> {
                print(red() + "Por favor insira um valor válido.\n\n" + reset())
                continue
            }
        }
    }
}

fun spacerTop (size: Int) {
    print("╔")
    for (index in 1 .. size) {
        print("═")
    }
    print("╗\n")
}

fun spacerBotton (size: Int) {
    print("╚")
    for (index in 1 .. size) {
        print("═")
    }
    print("╝\n")
}

fun table (data: List<String>) {
    var sizeTable = 120
    var sizeCell = 30

    for (line in data) {
        val fields = line.split(",")
        if (fields[1].length > sizeCell) {
            sizeCell = fields[1].length
            sizeTable = fields[1].length * 3
        }
        if (fields[2].length > sizeCell) {
            sizeCell = fields[2].length
            sizeTable = fields[2].length * 3
        }
        if (fields[3].length > sizeCell) {
            sizeCell = fields[3].length
            sizeTable = fields[4].length * 3
        }
    }

    spacerTop(sizeTable)
    cell(sizeCell, "Id")
    cell(sizeCell, "Nome")
    cell(sizeCell, "Cargo")
    cell(sizeCell, "Salário")
    print(" ║\n")
    tableDivisor(sizeTable)

    for (line in data) {
        val fields = line.split(",")
        cell(sizeCell, fields[0])
        cell(sizeCell, fields[1])
        cell(sizeCell, fields[2])
        cell(sizeCell, fields[3])
        print(" ║\n")
    }
    spacerBotton(sizeTable)
}

fun cell (size: Int, data: String) {
    val text = "║ $data"
    val newSize = size - text.length

    print(text)
    for (i in 1 .. newSize) {
        print(" ")
    }
}

fun tableDivisor(size: Int) {
    print("╠")
    for (index in 1 .. size) {
        print("═")
    }
    print("╣\n")
}